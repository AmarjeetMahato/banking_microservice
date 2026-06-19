package com.auth.domain.Authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponse {
    private String userId;
    private String email;
    private String role;
}
