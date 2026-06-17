package com.payment_service.domain.Payment.dtos;


import com.payment_service.domain.Payment.enums.CurrencyCode;
import com.payment_service.domain.Payment.enums.PaymentMethod;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import com.payment_service.domain.Payment.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {


    private String id;

    private String paymentReference;

    private String sourceAccountId;

    private String destinationAccountId;

    private BigDecimal amount;

    private CurrencyCode currency;

    private PaymentType paymentType;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String description;

    private String initiatedBy;

    private Boolean refunded;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

}
