package com.kyc_service.domain.kycdocument.service;

public class AadhaarValidator {

    // Verhoeff algorithm implementation (simplified usage wrapper)
    public static boolean isValidAadhaar(String aadhaar) {

        if (aadhaar == null || !aadhaar.matches("^[0-9]{12}$")) {
            return false;
        }

        return VerhoeffAlgorithm.validateVerhoeff(aadhaar);
    }
}
