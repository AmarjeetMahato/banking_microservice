package com.auth.domain.Users.controllers;


import com.auth.domain.Users.dtos.ResponseUserDto;
import com.auth.domain.Users.record.UserProfileResponse;
import com.auth.domain.Users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @GetMapping("/get_me")
    public ResponseEntity<?> getMe(Authentication authentication) {

        Map<String, Object> principal =
                (Map<String, Object>) authentication.getPrincipal();

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String remoteAddress = null;

        if (authentication.getDetails() instanceof WebAuthenticationDetails details) {
            remoteAddress = details.getRemoteAddress();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication.isAuthenticated());
        response.put("userId", principal.get("userId"));
        response.put("email", principal.get("email"));
        response.put("roles", roles);
        response.put("remoteAddress", remoteAddress);

        return ResponseEntity.ok(response);
    }
}
