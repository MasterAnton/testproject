package com.example.testproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class UserDTO extends AbstractDTO {
    private String name;
    private String nickname;
    private String email;
    private MediaDTO ava;
    private String profession;
    private String sitename;
    private String siteurl;
    private String phone;
    private Boolean isMyFollower;
    private Boolean isMyInfluencer;
    private Integer cntFollowers;
}
