package com.auth.domain.Authentication.controllers;


import com.auth.domain.Authentication.dtos.AuthLoginDto;
import com.auth.domain.Authentication.dtos.VerifyEmailDto;
import com.auth.domain.Authentication.services.AuthenticationService;
import com.auth.domain.Users.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

     private  final AuthenticationService authService;


     @GetMapping("/health")
     public  ResponseEntity<?> healthCheck(){
         return  ResponseEntity.status(HttpStatus.OK).body("Api Working Perfectly");
     }


     @PostMapping("/create")
     public ResponseEntity<String> createUser(@Valid @RequestBody UserDto createUserDto){
           this.authService.CreateUser(createUserDto);
           return  ResponseEntity.status(HttpStatus.ACCEPTED)
                   .body("User registered successfully. A verification email has been sent to your inbox.");
     }

     @PostMapping("/verify-email")
     public ResponseEntity<String> verifyEmail(@Valid
                     @RequestBody VerifyEmailDto verifyEmailDto,
                     HttpServletRequest request,
                     HttpServletResponse response) {
           String verify = authService.verifyEmail(verifyEmailDto,request,response);
           return  ResponseEntity.status(HttpStatus.OK).body(verify);
     }

     @PostMapping("/login")
     public ResponseEntity<String> loginUser(@Valid
                             @RequestBody AuthLoginDto loginDto,
                              HttpServletRequest request,
                              HttpServletResponse response
     ){
            String Login = authService.login(loginDto,request, response);
            return  ResponseEntity.status(HttpStatus.OK).body(Login);
     }

     @GetMapping("/get_me")
     public ResponseEntity<Authentication> getMe(){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         return ResponseEntity.status(HttpStatus.OK).body(authentication);
    }
}
