package com.deeba.botpersona.repository;

import java.util.List;
// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.security.core.userdetails.User;

import com.deeba.botpersona.model.UserEntity;
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByOwner(Long id);
}

