package com.kyc_service.domain.KycVerificationLog.dtos;

import com.kyc_service.domain.KycProfile.enums.KycStatus;
import com.kyc_service.domain.KycVerificationLog.enums.VerificationAction;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycVerificationLogResponseDto {

    private String id;

    private String kycProfileId;

    private VerificationAction action;

    private KycStatus oldStatus;

    private KycStatus newStatus;

    private String performedBy;

    private String performedRole;

    private LocalDateTime actionTime;

    private String remarks;

    private String ipAddress;

    private String deviceInfo;

    private String referenceId;

    private Boolean systemGenerated;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean active;

    private Boolean deleted;
}
