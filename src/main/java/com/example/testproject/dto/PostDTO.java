package com.example.testproject.dto;

import com.example.testproject.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class PostDTO extends AbstractDTO{
    private UserDTO author=null;
    private List<MediaDTO> medias=new ArrayList<>();
    private List<MultipartFile> files=new ArrayList<>();
    private String description=null;
    private User client=null;
    private Integer coverIndex=0;
    private Integer likeCnt=0;
    private Boolean like=false;
    private Boolean publicated=false;
    private Boolean favorite=false;
}
