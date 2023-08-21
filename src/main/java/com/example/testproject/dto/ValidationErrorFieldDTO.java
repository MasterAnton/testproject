package com.example.testproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ValidationErrorFieldDTO {
    private String name;
    private String message;
}
