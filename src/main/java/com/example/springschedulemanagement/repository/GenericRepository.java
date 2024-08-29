package com.example.springschedulemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);
}
