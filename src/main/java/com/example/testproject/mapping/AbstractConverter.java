package com.example.testproject.mapping;
import org.modelmapper.ModelMapper;
import com.example.testproject.dto.AbstractDTO;
import com.example.testproject.entity.AbstractEntity;

public abstract class AbstractConverter<S extends AbstractEntity, T extends AbstractDTO> implements Converter<S, T> {

    protected final ModelMapper modelMapper;
    protected final Class<T> dtoClass;
    protected final Class<S> entityClass;

    public AbstractConverter(ModelMapper modelMapper, Class<S> entityClass, Class<T> dtoClass) {
        this.modelMapper = modelMapper;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    @Override
    public final T map(S entity) {
        T mapped = modelMapper.map(entity, dtoClass);
        return mapped;
    }

    @Override
    public final S map(T dto) {
        S mapped = modelMapper.map(dto, entityClass);
        return mapped;
    }


}
