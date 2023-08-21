package com.example.testproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class MediaDTO extends AbstractDTO {
    private UserDTO author;
    private String url;
    private String thumbGridUrl;
    private String localThumbGridUrl;
    private String localUrl;
}
