package com.example.testproject.services;

import com.example.testproject.ApplicationContextProvider;
import com.example.testproject.StorageService;
import com.example.testproject.dto.PostDTO;
import com.example.testproject.entity.Media;
import com.example.testproject.entity.Post;
import com.example.testproject.entity.User;
import com.example.testproject.mapping.UserConverter;
import com.example.testproject.repository.MediaRepo;
import com.example.testproject.repository.PostRepo;
import com.example.testproject.repository.UsersRepo;
import com.example.testproject.utils.ImageHandler;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService extends AbstractEntityService<Post, PostDTO> implements PostEntityService{

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    UsersRepo usersRepo;

    Path rootLocation;

    @Autowired
    UserConverter userConverter;
    private final StorageService storageService;

    @Autowired
    public PostService(StorageService storageService) {
        this.storageService = storageService;
        storageService.init();
        ApplicationContextProvider appContext = new ApplicationContextProvider();
        rootLocation = Paths.get(Objects.requireNonNull(
                appContext.getApplicationContext().getEnvironment().getProperty("upload.path")
        ));
    }

    @Override
    public PostDTO add(PostDTO dto) throws AccessDeniedException {
        Post post = converter.map(dto);
        post = entityRepository.save(post);
        for (MultipartFile file:dto.getFiles() ) {
            Media media = new Media();
            media.setAuthor(userConverter.map(dto.getAuthor()));
            String originFileName=storageService.store(file);
            media.setLocalUrl(originFileName);
            try {
                IImageMetadata metadata = Sanselan.getMetadata(
                        rootLocation.resolve(originFileName).toFile()
                );
                MultipartFile mf = ImageHandler.generateThumb(file,"thumb_grid",300);
                media.setLocalThumbGridUrl(storageService.store(
                        mf,metadata
                )  );
            } catch (IOException | ImageReadException e) {
                e.printStackTrace();
            }
            media.setPost(post);
            mediaRepo.save(media);
        }
        return converter.map(post);
    }
    @Override
    public PostDTO update(PostDTO dto) throws AccessDeniedException {
        Post post = entityRepository.findById(dto.getId()).get();
        post.setCoverindex(dto.getCoverIndex());
        post.setDescription(dto.getDescription());
        post = entityRepository.save(post);

        List<String> newMedias = new ArrayList<>();
        dto.getMedias().forEach(mediaDto->
                newMedias.add(mediaDto.getLocalUrl())
        );
        mediaRepo.findByPost(post).forEach( media ->{
                if (!newMedias.contains(media.getLocalUrl())){
                    media.setPost(null);
                    mediaRepo.save(media);
                }
            }
        );

        for (MultipartFile file:dto.getFiles() ) {
            Media media = new Media();
            media.setAuthor(userConverter.map(dto.getAuthor()));
            String originFileName = storageService.store(file);
            media.setLocalUrl(originFileName);

            try {
                IImageMetadata metadata = Sanselan.getMetadata(
                        rootLocation.resolve(originFileName).toFile()
                );
                MultipartFile thumbMultipartFile = ImageHandler.generateThumb(file,"thumb_grid",300);
                String thumbFileName = storageService.store(
                        thumbMultipartFile,metadata
                );
                media.setLocalThumbGridUrl( thumbFileName );
            } catch (IOException | ImageReadException e) {
                e.printStackTrace();
            }
            media.setPost(post);
            mediaRepo.save(media);
        }
        return converter.map(post);
    }


    public List<PostDTO> findByAuthor(User author) {
        return postRepo.findByDeletedFalseAndAuthorOrderByCreatedAtDesc(author)
                .stream()
                .map(converter::map)
                .collect(Collectors.toList());
    }
    public List<PostDTO> findFavoriteByAuthor(User author) {
        List<Post> l = usersRepo.findById(author.getId()).get().getFavorites();
        Collections.reverse(l);

        return l
                .stream()
                .map(converter::map)
                .collect(Collectors.toList());
    }
}
