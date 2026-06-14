package com.auth.domain.Users.controllers;


import com.auth.domain.Users.dtos.ResponseUserDto;
import com.auth.domain.Users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllers {

    private final UserService userService;

    @PostMapping("/get_by_Id")
    public ResponseEntity<ResponseUserDto> getUser(){
        ResponseUserDto userDto = userService.getUserById("");
        return  ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping("/get_by_email")
    public ResponseEntity<ResponseUserDto> getUserByEmail(){
        ResponseUserDto userDto = userService.getUserByEmail("");
        return  ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

}
