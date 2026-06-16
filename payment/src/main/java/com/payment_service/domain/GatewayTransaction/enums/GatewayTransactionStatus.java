package com.payment_service.domain.GatewayTransaction.enums;

public enum GatewayTransactionStatus {
    INITIATED,

    PROCESSING,

    SUCCESS,

    FAILED,

    TIMEOUT,

    CANCELLED
}
