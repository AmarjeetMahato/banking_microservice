package com.auth.domain.Roles.dtos;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoleDto {

    @Size(max = 50, message = "Role name must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Z_]+$",
            message = "Role name must be uppercase and can contain underscores only"
    )
    private String roleName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
