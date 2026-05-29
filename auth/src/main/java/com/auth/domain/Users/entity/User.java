package com.auth.domain.Users.entity;

import com.auth.base.BaseEntity;
import com.auth.domain.Roles.entity.Role;
import com.auth.domain.Tokens.entity.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_phone", columnList = "phone"),
        @Index(name = "idx_is_active", columnList = "isActive")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {

    @NotNull(message = "first name is required")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotNull(message = "Last name is required")
    @Column(name = "last_name", nullable = false, length = 100)
    private  String lastName;

    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = true, unique = true, length = 150)
    private  String Email;

    @Column(name = "phone", nullable = true,unique = true, length = 20)
    private String phone;

    @Column(name = "profile_pic")
    private  String profilePic;

    @NotBlank(message = "Gender is required")
    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean  isActive = true;

    @Builder.Default
    private boolean emailVerified = false; // Registration ke baad verification ke liye


    // --- Relation ---
    // User.java के अंदर जोड़ें
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles_mapping", // Junction table का नाम
            joinColumns = @JoinColumn(name = "user_id"), // User की ID
            inverseJoinColumns = @JoinColumn(name = "role_id") // Role की ID
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();

}
