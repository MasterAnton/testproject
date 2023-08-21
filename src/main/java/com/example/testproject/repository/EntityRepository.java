package com.example.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface EntityRepository<T> extends JpaRepository<T, Long> {

    @Override
    List<T> findAll();

    @Override
    Optional<T> findById(Long id);

    default Optional<T> findByIdentifier(String identifier) {
        throw new UnsupportedOperationException();
    }

    default Long deleteByIdentifier(String identifier) {
        throw new UnsupportedOperationException();
    }

    default boolean existsByIdentifier(String identifier) {
        throw new UnsupportedOperationException();
    }

}