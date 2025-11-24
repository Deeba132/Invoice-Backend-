package com.deeba.botpersona.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.deeba.botpersona.model.LoginEntity;
import com.deeba.botpersona.model.UserEntity;
import com.deeba.botpersona.repository.LoginRepo;
import com.deeba.botpersona.repository.UserRepo;


import lombok.Data;

@RestController
    @RequestMapping("/api/add")
    class ItemController{

        @Autowired
        private LoginRepo loginRepo;
        @Autowired
        private UserRepo userRepo;

         @PostMapping("/items")
        public ResponseEntity<?> dets(@RequestBody ItemsRequest itemsRequest){
            UserEntity userEntity=new UserEntity();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            LoginEntity login=loginRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
            userEntity.setName(itemsRequest.getName());
            Double unitPrice=itemsRequest.getPrice();
            int quantity=itemsRequest.getQuantity();
            userEntity.setPrice(unitPrice*quantity);
            userEntity.setQuantity(quantity);
            userEntity.setOwner(login);
            userRepo.save(userEntity);
            return ResponseEntity.ok(Map.of("message","items added successfully"));
        } 

        @GetMapping("/items/all")
        public @ResponseBody List<UserEntity> getAllUserEntities(){
            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            LoginEntity login=loginRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
             return userRepo.findByOwnerId(login.getId());
        }

        @DeleteMapping("/items/{id}")
        public ResponseEntity<String> clearu(@PathVariable Long id){
              String username=SecurityContextHolder.getContext().getAuthentication().getName();
              LoginEntity login=loginRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
              UserEntity item=userRepo.findById(id).orElseThrow(()->new RuntimeException("invalid user"));
              if(!item.getOwner().getId().equals(login.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cant delete the record");
              }
              userRepo.deleteById(id);
              return ResponseEntity.ok("record deleted");
        }

        @PutMapping("/items/update/{id}")
        public ResponseEntity<?> update(@PathVariable Long id,@RequestBody ItemsRequest itemsRequest){
            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            LoginEntity login=loginRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
            UserEntity userRecord=userRepo.findById(id).orElseThrow(()->new RuntimeException("invalid id"));
            if(!userRecord.getOwner().getId().equals(login.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cant update the item");
            }
            if(itemsRequest.getName()!=null&& !itemsRequest.getName().isEmpty() ){
                userRecord.setName(itemsRequest.getName());
            }
            Double price=itemsRequest.getPrice()!=null?itemsRequest.getPrice():userRecord.getPrice()/userRecord.getQuantity();
            Integer quantity=itemsRequest.getQuantity()!=null?itemsRequest.getQuantity():userRecord.getQuantity();
            userRecord.setPrice(price*quantity);
            userRecord.setQuantity(quantity);
            userRepo.save(userRecord);
            return ResponseEntity.ok(Map.of("message","Updated successfully"));
        }
    }

@Data
    class ItemsRequest{
        private String name;
        private Double price;
        private Integer quantity;
        private Long user_Id;
    }


