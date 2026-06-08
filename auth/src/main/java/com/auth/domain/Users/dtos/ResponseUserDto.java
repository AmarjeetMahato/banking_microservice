package com.auth.domain.Users.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String profilePic;

    private String gender;

    private boolean isActive;

    private boolean emailVerified;

    private Set<String> roles;
}
