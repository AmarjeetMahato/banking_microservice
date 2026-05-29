package com.auth.domain.Roles.services;

import com.auth.domain.Roles.dtos.ResponseRoleDto;
import com.auth.domain.Roles.dtos.RoleDto;
import com.auth.domain.Roles.dtos.UpdateRoleDto;

import java.util.List;

public interface RoleService {

    ResponseRoleDto createRole(RoleDto rolesDto);

    ResponseRoleDto updateRole(UpdateRoleDto updateRoleDto, String roleId);

    ResponseRoleDto getRoles(String roleId);

    List<ResponseRoleDto> getAllRoles(int limit, int page);

    void deleteRole(String roleId);
}
