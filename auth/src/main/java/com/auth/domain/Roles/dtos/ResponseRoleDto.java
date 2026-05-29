package com.auth.domain.Roles.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseRoleDto {

    private String id;
    private String roleName;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
