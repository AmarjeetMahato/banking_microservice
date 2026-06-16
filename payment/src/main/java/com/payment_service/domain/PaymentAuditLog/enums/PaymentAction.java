package com.payment_service.domain.PaymentAuditLog.enums;

public enum PaymentAction {
    CREATED,

    PROCESSING_STARTED,

    APPROVED,

    REJECTED,

    SUCCESS,

    FAILED,

    CANCELLED,

    REFUNDED,

    REVERSAL,

    STATUS_CHANGED
}
