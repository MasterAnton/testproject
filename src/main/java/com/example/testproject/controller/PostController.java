package com.example.testproject.controller;

import com.example.testproject.dto.MediaDTO;
import com.example.testproject.dto.PostDTO;
import com.example.testproject.entity.*;
import com.example.testproject.mapping.UserConverter;
import com.example.testproject.services.PostEntityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.testproject.repository.PostRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("api/posts")
@Slf4j
public class PostController{

//    @Autowired
//    private PostEntityService postService;

//    @PostMapping
//    public ResponseEntity<List<PostDTO>> addPost(@RequestBody PostDTO dto) {
//        log.info("POST request with data: {}", dto);
////        List<PostDTO> res = Collections.singletonList(postService.add(dto));
//        return new ResponseEntity<>(null, HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<PostDTO>> getPosts() {
//        return new ResponseEntity<>(postService.findAll(), HttpStatus.OK);
//    }

    @Autowired
    private PostEntityService postService;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("my")
    public ResponseEntity<List<PostDTO>> getMyPosts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(postService.findByAuthor((User)auth.getPrincipal()), HttpStatus.OK);
    }
    @GetMapping("favorite")
    public ResponseEntity<List<PostDTO>> getMyFavorite() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(postService.findFavoriteByAuthor((User)auth.getPrincipal()), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PostDTO> getPost(@RequestParam Long id) {
        PostDTO post = postService.findById(id);
        if (!post.getPublicated()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User current = (User) auth.getPrincipal();
            if (!current.getId().equals( post.getAuthor().getId()))
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }


    @Autowired
    UserConverter userConverter;

    private List<String> parseJsonToArray(String s) throws ParseException {
        ArrayList<Object> arr;
        JSONParser jsonParser=new JSONParser(s);
        arr = jsonParser.parseArray();
        ArrayList<String> res= new ArrayList();
        arr.forEach(o -> res.add(o.toString()));
        return res;
    }

    @RequestMapping(method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String addOrEditPost(
            @RequestParam(name="files",required=false) List<MultipartFile> files,
            @RequestPart(required = false) String description,
            @RequestPart   String coverindex,
            @RequestPart   String id,
            @RequestPart   String medias
    ) throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current = (User) auth.getPrincipal();
        if(files==null) files = new ArrayList();
        if(id.equals("") || id.equals("null")){
            if(files.isEmpty()) return "OK";
            PostDTO dto = new PostDTO();
            dto.setAuthor(userConverter.map(current));
            dto.setFiles(files);

            if (description!=null) dto.setDescription(description.trim());
            dto.setCoverIndex(Integer.parseInt(coverindex));
            postService.add(dto);
        }
        else{
            PostDTO dto = postService.findById(Long.parseLong(id));
            if (Objects.equals(current.getId(), dto.getAuthor().getId())){
                List<String> oldMedias = parseJsonToArray(medias);
                ArrayList<MediaDTO> newMedias= new ArrayList();
                dto.getMedias().forEach(m ->{
                        if(oldMedias.contains(m.getLocalUrl())){
                              newMedias.add(m);
                        }
                    }
                );
                dto.setMedias(newMedias);
                if(files.isEmpty() && dto.getMedias().isEmpty()) return "OK";
                dto.setFiles(files);
                if (description!=null) dto.setDescription(description.trim());
                dto.setCoverIndex(Integer.parseInt(coverindex));
                postService.update(dto);

            }
        }
        return "OK";
    }

    @RequestMapping(path="delete",method = POST)
    public ResponseEntity<PostDTO> deletePost(
            @RequestPart   String id
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User current = (User) auth.getPrincipal();
        Optional<Post> postOpt = postRepo.findById(Long.parseLong(id));
        PostDTO res=null;
        if(postOpt.isPresent() && postOpt.get().getAuthor().getId().equals(current.getId())){
            Post p = postOpt.get();
            p.setDeleted(true);
            postRepo.save(p);
        }
        return new ResponseEntity<>( res, HttpStatus.OK);
    }



}
