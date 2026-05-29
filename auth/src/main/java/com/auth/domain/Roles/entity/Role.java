package com.auth.domain.Roles.entity;


import com.auth.base.BaseEntity;
import com.auth.domain.Users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role extends BaseEntity {

    @NotNull(message = "Role name can't be null")
    @Column(name = "roles", nullable = false , unique = true)
    private  String roleName;

    @Column(length = 255)
    private String description; // Role का काम समझाने के लिए

    // Many-to-Many Relationship with User
    // Roles टेबल में हम ये देख सकते हैं कि इसमें कौन-कौन से यूजर्स हैं
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

}
