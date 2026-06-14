package com.auth.domain.Users.services;

import com.auth.domain.Users.dtos.ResponseUserDto;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.mapper.UserMapper;
import com.auth.domain.Users.repository.UserRepository;
import com.auth.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService {

    private  final UserRepository userRepository;
    private  final UserMapper userMapper;

    @Override
    public ResponseUserDto getUserById(String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    ()-> new ResourceNotFoundException("User not found")
            );
            return  userMapper.toResponse(user);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseUserDto getUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    ()-> new ResourceNotFoundException("User not found")
            );
            return  userMapper.toResponse(user);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
