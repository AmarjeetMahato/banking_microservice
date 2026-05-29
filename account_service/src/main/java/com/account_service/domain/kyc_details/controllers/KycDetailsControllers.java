package com.account_service.domain.kyc_details.controllers;


import com.account_service.domain.kyc_details.services.KycDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kycdetail")
@RequiredArgsConstructor
public class KycDetailsControllers {

    private  final KycDetailsService kycDetailsService;


}
