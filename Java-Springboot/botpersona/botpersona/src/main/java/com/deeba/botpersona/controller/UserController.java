package com.deeba.botpersona.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.deeba.botpersona.service.UserServiceImpl;
import com.deeba.botpersona.model.UserEntity;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userService;
    @Autowired
    public UserController(UserServiceImpl userService){
        this.userService=userService;
    }
    @GetMapping
    public List<UserEntity> getAll(){
        return userService.getAllUsers();
    }
}
