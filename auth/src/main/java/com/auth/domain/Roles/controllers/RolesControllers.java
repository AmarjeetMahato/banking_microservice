package com.auth.domain.Roles.controllers;


import com.auth.domain.Roles.dtos.ResponseRoleDto;
import com.auth.domain.Roles.dtos.RoleDto;
import com.auth.domain.Roles.dtos.UpdateRoleDto;
import com.auth.domain.Roles.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RolesControllers {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<ResponseRoleDto> crateRoles(@Valid @RequestBody RoleDto rolesDto){
        ResponseRoleDto responseDto = roleService.createRole(rolesDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{roleId}/update")
    public  ResponseEntity<ResponseRoleDto> upateRole(
            @Valid
            @RequestBody UpdateRoleDto updateRoleDto,
            @PathVariable String roleId){
        ResponseRoleDto responseDto = roleService.updateRole(updateRoleDto,roleId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{roleId}/get_role")
    public ResponseEntity<ResponseRoleDto> fetchRoleById(@PathVariable String roleId){
        ResponseRoleDto responseDto = roleService.getRoles(roleId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/get_all_roles")
    public ResponseEntity<List<ResponseRoleDto>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<ResponseRoleDto> response = roleService.getAllRoles(page, limit);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roleId}/delete")
    public ResponseEntity<?> deleteRole(@PathVariable String roleId){
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
