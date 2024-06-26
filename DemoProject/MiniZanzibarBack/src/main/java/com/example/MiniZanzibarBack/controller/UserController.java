package com.example.MiniZanzibarBack.controller;

import com.example.MiniZanzibarBack.config.JwtService;
import com.example.MiniZanzibarBack.dto.LoginResponseDTO;
import com.example.MiniZanzibarBack.dto.UserLoginDTO;
import com.example.MiniZanzibarBack.dto.UserRegistrationDTO;
import com.example.MiniZanzibarBack.exception.UserAlreadyExistsException;
import com.example.MiniZanzibarBack.model.User;
import com.example.MiniZanzibarBack.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final IUserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(IUserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        try {
            User authenticatedUser = userService.loginUser(loginDTO);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponseDTO loginResponse = new LoginResponseDTO(authenticatedUser.getUsername(), jwtToken);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
