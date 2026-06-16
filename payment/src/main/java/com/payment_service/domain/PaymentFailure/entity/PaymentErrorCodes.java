package com.payment_service.domain.PaymentFailure.entity;

public class PaymentErrorCodes {
    private PaymentErrorCodes() {}

    public static final String INSUFFICIENT_BALANCE =
            "INSUFFICIENT_BALANCE";

    public static final String DAILY_LIMIT_EXCEEDED =
            "DAILY_LIMIT_EXCEEDED";

    public static final String ACCOUNT_BLOCKED =
            "ACCOUNT_BLOCKED";

    public static final String BENEFICIARY_NOT_FOUND =
            "BENEFICIARY_NOT_FOUND";

    public static final String GATEWAY_TIMEOUT =
            "GATEWAY_TIMEOUT";

    public static final String NETWORK_ERROR =
            "NETWORK_ERROR";

    public static final String DUPLICATE_PAYMENT =
            "DUPLICATE_PAYMENT";

    public static final String FRAUD_SUSPECTED =
            "FRAUD_SUSPECTED";
}
