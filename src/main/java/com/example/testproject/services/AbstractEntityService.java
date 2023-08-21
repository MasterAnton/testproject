package com.example.testproject.services;

import com.example.testproject.mapping.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import com.example.testproject.dto.AbstractDTO;
import com.example.testproject.entity.AbstractEntity;
import com.example.testproject.repository.EntityRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEntityService<TE extends AbstractEntity, TO extends AbstractDTO> implements EntityService<TO> {

    protected EntityRepository<TE> entityRepository;

    protected Converter<TE, TO> converter;

    @Autowired
    public void setEntityRepository(EntityRepository<TE> entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Autowired
    public void setConverter(Converter<TE, TO> converter) {
        this.converter = converter;
    }

    @Override
    public List<TO> findAll() {
        return entityRepository.findAll()
                .stream()
                .map(converter::map)
                .collect(Collectors.toList());
    }

    protected TE findEntityById(Long id) throws EntityNotFoundException {
        return entityRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public TO findById(Long id) throws EntityNotFoundException {
        return converter.map(entityRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public TO add(TO dto) {
        TE entity = entityRepository.save(converter.map(dto));
        TO dtoBack = converter.map(entity);
        processAddPushMessages(entity);
        return dtoBack;
    }

    @Override
    public TO update(TO dto) throws EntityNotFoundException, AccessDeniedException {
        if (entityRepository.existsById(dto.getId())) {
            TE result = entityRepository.save(converter.map(dto));
            processUpdatePushMessages(result);
            return converter.map(result);
        } else {
            throw new EntityNotFoundException();
        }
    }

    protected void processUpdatePushMessages(TE entity) {
    }

    protected void processAddPushMessages(TE entity) {
    }


}