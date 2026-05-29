package com.auth.domain.Roles.mapper;

import com.auth.domain.Roles.dtos.ResponseRoleDto;
import com.auth.domain.Roles.dtos.RoleDto;
import com.auth.domain.Roles.dtos.UpdateRoleDto;
import com.auth.domain.Roles.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    // DTO → ENTITY
    public Role toEntity(RoleDto dto){

        if(dto == null) {
            throw new   IllegalArgumentException("Role Dto must not be null");
        }
        return  Role.builder()
                .roleName(dto.getRoleName())
                .description(dto.getDescription())
                .build();
    }


    public void updateEntity(Role role, UpdateRoleDto dto) {

        if (role == null) {
            throw new IllegalArgumentException("Role entity must not be null");
        }

        if (dto == null) {
            throw new IllegalArgumentException("UpdateRoleDto must not be null");
        }

        // Update only if not null (supports partial update)
        if (dto.getRoleName() != null) {
            role.setRoleName(dto.getRoleName());
        }

        if (dto.getDescription() != null) {
            role.setDescription(dto.getDescription());
        }
    }

    public ResponseRoleDto toResponse(Role roles){
        if(roles == null){
            throw  new IllegalArgumentException("Roles must not be null");
        }

        return  ResponseRoleDto
                .builder()
                .roleName(roles.getRoleName())
                .description(roles.getDescription())
                .createdAt(roles.getCreatedAt())
                .updatedAt(roles.getUpdatedAt())
                .build();
    }
}
