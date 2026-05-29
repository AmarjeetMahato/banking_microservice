package com.account_service.domain.audit_logs.entity;

import com.account_service.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "audit_logs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog extends BaseEntity {

    @NotNull(message = "Account ID is required")
    @Column(nullable = false)
    private UUID accountId;

    @NotNull(message = "Performer user ID is required")
    @Column(nullable = false)
    private UUID performedBy;

    @NotBlank(message = "Audit action description cannot be blank")
    @Size(max = 100, message = "Action description cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String action;

    @Size(max = 50, message = "IP Address cannot exceed 50 characters")
    @Pattern(
            regexp = "^([0-9a-fA-F:.]*)$",
            message = "Invalid characters detected in IP Address format"
    )
    @Column(length = 50)
    private String ipAddress;

    @Size(max = 2000, message = "User agent string is unusually long")
    @Column(columnDefinition = "TEXT")
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String details;
}
