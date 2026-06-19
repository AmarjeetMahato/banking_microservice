package com.auth.config;


import com.auth.domain.Roles.entity.Role;
import com.auth.domain.Roles.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDatabaseSetup implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Automatically checks and creates your initial application roles
        createRoleIfMissing("USER", "Default application access for registered users.");
        createRoleIfMissing("ADMIN", "Administrative privileges for platform management.");
    }

    private void createRoleIfMissing(String roleName, String description) {
        // Query database to see if the role name already exists
        if (!roleRepository.existsByRoleName(roleName)) {
            Role role = new Role();
            role.setRoleName(roleName);
            role.setDescription(description);

            roleRepository.save(role);
            System.out.println("🚀 Successfully populated initial role: " + roleName);
        } else {
            System.out.println("ℹ️ Role " + roleName + " already exists. Skipping initialization.");
        }
    }
}
