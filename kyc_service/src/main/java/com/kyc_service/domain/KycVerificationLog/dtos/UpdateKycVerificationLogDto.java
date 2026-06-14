package com.kyc_service.domain.KycVerificationLog.dtos;

import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycVerificationLog.enums.VerificationAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKycVerificationLogDto {
    @NotNull(message = "Verification action is required")
    private VerificationAction action;

    private KycStatus oldStatus;

    private KycStatus newStatus;

    @NotBlank(message = "Performed by is required")
    @Size(
            min = 2,
            max = 100,
            message = "Performed by must be between 2 and 100 characters"
    )
    private String performedBy;

    @Size(
            max = 50,
            message = "Performed role cannot exceed 50 characters"
    )
    private String performedRole;

    @NotNull(message = "Action time is required")
    private LocalDateTime actionTime;

    @Size(
            max = 1000,
            message = "Remarks cannot exceed 1000 characters"
    )
    private String remarks;

    @Size(
            max = 50,
            message = "IP address cannot exceed 50 characters"
    )
    private String ipAddress;

    @Size(
            max = 500,
            message = "Device information cannot exceed 500 characters"
    )
    private String deviceInfo;

    @Size(
            max = 100,
            message = "Reference ID cannot exceed 100 characters"
    )
    private String referenceId;

    private Boolean systemGenerated;
}
