package com.example.springschedulemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {
    // 필요한 경우 JpaRepository에서 자동으로 제공받는것 외에 커스텀 메서드 추가할 것
}
