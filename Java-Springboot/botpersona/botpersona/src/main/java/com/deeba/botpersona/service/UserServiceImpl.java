package com.deeba.botpersona.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.deeba.botpersona.repository.UserRepo;
import com.deeba.botpersona.model.UserEntity;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    @Autowired
    public UserServiceImpl(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    @Override
    public List<UserEntity> getAllUsers(){
        return userRepo.findAll();
    }
}

