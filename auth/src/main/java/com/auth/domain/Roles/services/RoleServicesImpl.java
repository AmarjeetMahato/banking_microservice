package com.auth.domain.Roles.services;


import com.auth.domain.Roles.dtos.ResponseRoleDto;
import com.auth.domain.Roles.dtos.RoleDto;
import com.auth.domain.Roles.dtos.UpdateRoleDto;
import com.auth.domain.Roles.entity.Role;
import com.auth.domain.Roles.mapper.RoleMapper;
import com.auth.domain.Roles.repository.RoleRepository;
import com.auth.globalException.BadRequestException;
import com.auth.globalException.InternalServerError;
import com.auth.globalException.ResourceAlreadyExistsException;
import com.auth.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleServicesImpl implements  RoleService {

    private final RoleRepository roleRepository;
    private  final RoleMapper roleMapper;


    @Override
    public ResponseRoleDto createRole(RoleDto rolesDto) {
        roleRepository.findByRoleName(rolesDto.getRoleName()).ifPresent(roles -> {
                    throw new ResourceAlreadyExistsException(
                            "Role already exists with name: " + rolesDto.getRoleName()
                    );
                }
        );
        try {
            Role role = roleMapper.toEntity(rolesDto);
            Role saved = roleRepository.save(role);
            return roleMapper.toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceAlreadyExistsException("Database constraint violation: Role may already exist");
        } catch (RuntimeException e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    @Override
    public ResponseRoleDto updateRole(UpdateRoleDto updateRoleDto, String roleId) {
        try{
            Role existing =   roleRepository.findById(roleId).orElseThrow(()->
                    new ResourceNotFoundException("Role Not found"));
            roleMapper.updateEntity(existing,updateRoleDto);
            Role save = roleRepository.save(existing);
            return  roleMapper.toResponse(save);
        } catch (RuntimeException e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    @Override
    public ResponseRoleDto getRoles(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id: " + roleId)
                );
        return roleMapper.toResponse(role);
    }

    @Override
    public List<ResponseRoleDto> getAllRoles(int limit, int page) {
        // ✅ validation
        if (page < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0");
        }

        if (limit <= 0) {
            throw new BadRequestException("Limit must be greater than 0");
        }

        if (limit > 100) {
            throw new BadRequestException("Limit must not exceed 100");
        }


        Pageable pageable = PageRequest.of(page, limit);

        List<Role> roles = roleRepository.findAll(pageable)
                .getContent();

        return roles.stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found with id: " + roleId)
                );
        roleRepository.delete(role);
    }
}
