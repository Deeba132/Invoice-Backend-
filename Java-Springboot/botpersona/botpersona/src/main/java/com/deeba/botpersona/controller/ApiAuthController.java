    package com.deeba.botpersona.controller;

    import com.deeba.botpersona.model.LoginEntity;

import com.deeba.botpersona.repository.LoginRepo;

import com.deeba.botpersona.security.JWTUtil;
    import lombok.Data;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/auth")
    public class ApiAuthController {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private LoginRepo loginRepo;

        @Autowired
        private AuthenticationManager authenticationManager;


        @Autowired
        private JWTUtil jwtUtil;

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest request){
            if (loginRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        LoginEntity user = new LoginEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        loginRepo.save(user);
       String token = jwtUtil.generateToken(user.getUsername());

    return ResponseEntity.ok(new LoginResponse(token, "User registered successfully")); 
}

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
                
                String username=authentication.getName();
                String token = jwtUtil.generateToken(username);
                return ResponseEntity.ok(new LoginResponse(token, "Login successful"));
            } catch (AuthenticationException e) {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        }

        @GetMapping("/test")
        public ResponseEntity<String> test() {
            return ResponseEntity.ok("Auth endpoint is working!");
        }
    }

    @Data
    class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    class RegisterRequest{
        private String password;
        private String username;
    }


    @Data
    class LoginResponse {
        private String token;
        private String message;

        public LoginResponse(String token, String message) {
            this.token = token;
            this.message = message;
        }
    }

