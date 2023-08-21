package com.example.testproject.services;

import com.example.testproject.dto.AbstractDTO;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface EntityService<TO extends AbstractDTO> {

    TO add(TO entity) throws UnsupportedOperationException, EntityExistsException;

    TO update(TO entity) throws UnsupportedOperationException, EntityNotFoundException, AccessDeniedException;

    List<TO> findAll();

    default TO findByIdentifier(String identifier) throws UnsupportedOperationException, EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

    default TO findById(Long id) throws UnsupportedOperationException, EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

}