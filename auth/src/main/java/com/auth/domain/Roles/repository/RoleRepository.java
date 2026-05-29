package com.auth.domain.Roles.repository;

import com.auth.domain.Roles.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository  extends JpaRepository<Role,String> {

    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

    List<Role> findByRoleNameIn(Set<String> roleNames);
}
