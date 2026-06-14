package com.auth.domain.Users.mapper;

import com.auth.domain.Roles.entity.Role;
import com.auth.domain.Users.dtos.ResponseUserDto;
import com.auth.domain.Users.dtos.UpdateUserDto;
import com.auth.domain.Users.dtos.UserDto;
import com.auth.domain.Users.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserDto dto){
        if (dto == null) return null;

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .deleted(false)
                .profilePic(dto.getProfilePic())
                .isActive(true)
                .emailVerified(false)
                .roles(new HashSet<>()) // assign later in service layer
                .build();
    }

    public void toUpdateEntity(UpdateUserDto dto, User user) {

        if (dto == null || user == null) return;

        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());

        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());

        if (dto.getPhone() != null)
            user.setPhone(dto.getPhone());

        if (dto.getGender() != null)
            user.setGender(dto.getGender());

        if (dto.getProfilePic() != null)
            user.setProfilePic(dto.getProfilePic());
    }

    public ResponseUserDto toResponse(User user) {

        if (user == null) return null;

        return ResponseUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profilePic(user.getProfilePic())
                .gender(user.getGender())
                .isActive(user.isActive())
                .emailVerified(user.isEmailVerified())
                .roles(
                        user.getRoles() == null
                                ? new HashSet<>()
                                : user.getRoles()
                                .stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
