package com.auth.domain.Tokens.entity;


import com.auth.base.BaseEntity;
import com.auth.domain.Tokens.enums.TokenType;
import com.auth.domain.Users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens", indexes = {
        @Index(name = "idx_token_value", columnList = "token_value") // Fast lookup for validation
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Token extends BaseEntity {

    @Column(name = "token_value", nullable = false, unique = true, columnDefinition = "TEXT")
    private String tokenValue; // Asal JWT ya Random String

    @Enumerated(EnumType.STRING)
    private TokenType tokenType; // REFRESH, EMAIL_VERIFICATION, PASSWORD_RESET

    private boolean revoked; // Kya token manually cancel kar diya gaya hai?
    private boolean expired; // Kya token expire ho chuka hai?

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate; // Token kab tak valid hai

    // Relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
