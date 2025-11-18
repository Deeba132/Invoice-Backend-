package com.deeba.botpersona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deeba.botpersona.model.LoginEntity;

public interface LoginRepo extends JpaRepository<LoginEntity,Long>{
    Optional<LoginEntity> findByUsername(String username);
}
