package com.example.testproject.mapping;

import com.example.testproject.dto.AbstractDTO;
import com.example.testproject.entity.AbstractEntity;

public interface Converter<S extends AbstractEntity, T extends AbstractDTO> {

    T map(S entity);

    S map(T dto);
}
