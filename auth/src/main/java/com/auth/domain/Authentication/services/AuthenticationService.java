package com.auth.domain.Authentication.services;

import com.auth.domain.Authentication.dtos.AuthLoginDto;
import com.auth.domain.Authentication.dtos.VerifyEmailDto;
import com.auth.domain.Users.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    String CreateUser(UserDto dto);

    String login(AuthLoginDto dto);

    String verifyEmail(VerifyEmailDto verifyEmailDto, HttpServletRequest request, HttpServletResponse response);
}
