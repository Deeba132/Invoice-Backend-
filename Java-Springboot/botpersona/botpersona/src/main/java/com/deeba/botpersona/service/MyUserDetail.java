package com.deeba.botpersona.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deeba.botpersona.model.LoginEntity;
// import com.deeba.botpersona.model.UserEntity;
import com.deeba.botpersona.repository.LoginRepo;
// import com.deeba.botpersona.repository.UserRepo;

@Service
public class MyUserDetail implements UserDetailsService{

    @Autowired
    private LoginRepo loginRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginEntity loginEntity=loginRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("invalid username"));


        return new org.springframework.security.core.userdetails.User(
            loginEntity.getUsername(),
            loginEntity.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}