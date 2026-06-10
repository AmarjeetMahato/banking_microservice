package com.auth.domain.Authentication.controllers;


import com.auth.domain.Authentication.dtos.CreateUserDto;
import com.auth.domain.Authentication.services.AuthenticationService;
import com.auth.domain.Users.dtos.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

     private  final AuthenticationService authService;


     @PostMapping("/create")
     public ResponseEntity<String> createUser(@Valid @RequestBody UserDto createUserDto){
           String user = this.authService.CreateUser(createUserDto);
           return  ResponseEntity.status(HttpStatus.CREATED).body(user);
     }



}
