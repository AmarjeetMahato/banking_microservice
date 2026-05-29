package com.auth.domain.Tokens.enums;

import lombok.Getter;

@Getter
public enum TokenType {
    /**
     * REFRESH: इसका उपयोग नया Access Token प्राप्त करने के लिए किया जाता है
     * ताकि यूजर को बार-बार लॉगिन न करना पड़े।
     */
    REFRESH,

    /**
     * EMAIL_VERIFICATION: नए अकाउंट को वेरीफाई करने के लिए ईमेल पर भेजा जाने वाला टोकन।
     */
    EMAIL_VERIFICATION,

    /**
     * PASSWORD_RESET: पासवर्ड भूल जाने पर उसे बदलने के लिए उपयोग किया जाने वाला सुरक्षित टोकन।
     */
    PASSWORD_RESET,

    /**
     * MFA_OTP: Multi-Factor Authentication (जैसे मोबाइल पर आने वाला 2FA कोड)।
     */
    MFA_OTP,

    /**
     * BEARER: स्टैंडर्ड JWT एक्सेस टोकन टाइप (आमतौर पर इसे डेटाबेस में स्टोर नहीं करते,
     * पर टाइप बताने के लिए काम आता है)।
     */
    BEARER,

    LOGIN
}
