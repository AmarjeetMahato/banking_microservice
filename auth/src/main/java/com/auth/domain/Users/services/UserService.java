package com.auth.domain.Users.services;

import com.auth.domain.Users.dtos.ResponseUserDto;

public interface UserService {

    ResponseUserDto getUserById(String userId);

    ResponseUserDto getUserByEmail(String email);
}
