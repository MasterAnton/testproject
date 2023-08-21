package com.example.testproject.mapping;

import com.example.testproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.testproject.dto.PostDTO;
import com.example.testproject.entity.Post;
import com.example.testproject.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter  implements Converter<Post, PostDTO>{
//    @Autowired
//    private Converter<Post, PostDTO> postConverter;
//
//    @Autowired
//    private EntityRepository<Post> postRepo;
//
//    @Autowired
//    private EntityRepository<Work> workRepo;

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    UserConverter userConverter;

    @Autowired
    MediaConverter mediaConverter;


    @Autowired
    PostRepo postRepo;

    @Override
    public PostDTO map(Post source) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = (User) auth.getPrincipal();

        PostDTO target = new PostDTO();

        target.setAuthor(
                userConverter.map(source.getAuthor())
        );
        target.setId(source.getId());
        target.setCoverIndex(source.getCoverindex());
        target.setDescription(source.getDescription());
        target.setPublicated(source.isPublicated());

        target.setFavorite( postRepo.isFavoriteOfUser(u.getId(),source.getId()) );
        target.setFavorite( usersRepo.findById(u.getId()).get().getFavorites().contains(source));

        target.setMedias(
            mediaRepo.findByPost(source)
                    .stream()
                    .map(mediaConverter::map)
                    .collect(Collectors.toList())
        );
        return target;
    }


    @Override
    public Post map(PostDTO target) {
        Post source = new Post();
        source.setAuthor(userConverter.map(target.getAuthor()));
        source.setCoverindex(target.getCoverIndex());
        source.setDescription(target.getDescription());
        source.setPublicated(target.getPublicated());
       return source;
    }
}
