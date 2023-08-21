package com.example.testproject.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ValidationErrorDTO extends ResponseDTO {
    private List<ValidationErrorFieldDTO> fields;
}
