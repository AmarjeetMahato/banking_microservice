package com.account_service.domain.kyc_details.controllers;


import com.account_service.domain.kyc_details.dtos.KycDetailsDto;
import com.account_service.domain.kyc_details.dtos.ResponseKycDetails;
import com.account_service.domain.kyc_details.dtos.UpdateKycDetails;
import com.account_service.domain.kyc_details.services.KycDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kycdetail")
@RequiredArgsConstructor
public class KycDetailsControllers {

    private  final KycDetailsService kycDetailsService;


    @PostMapping("/create")
    public ResponseEntity<ResponseKycDetails> createKycDetails(
            @Valid @RequestBody KycDetailsDto kycDetailsDto){
        ResponseKycDetails kycDetails = kycDetailsService.createKycDetails(kycDetailsDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(kycDetails);
    }

    @PatchMapping("/{id}/update")
    public  ResponseEntity<ResponseKycDetails> updateKycDetails(
            @PathVariable String id,
            @Valid @RequestBody UpdateKycDetails UpdatekycDetails
            ){
        ResponseKycDetails kycDetails = kycDetailsService.updateKycDetails(id,UpdatekycDetails);
        return  ResponseEntity.status(HttpStatus.OK).body(kycDetails);
    }

    @GetMapping("/{id}/get_by_Id")
    public  ResponseEntity<ResponseKycDetails> GetKYCDetail(@PathVariable String id){
        ResponseKycDetails kycDetails = kycDetailsService.getKycDetailsById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(kycDetails);
    }

    @GetMapping("/{accountId}/get_by_Id")
    public  ResponseEntity<ResponseKycDetails> GetKYCDetailByAccountId(@PathVariable String accountId){
        ResponseKycDetails kycDetails = kycDetailsService.getKycDetailsByAccountId(accountId);
        return  ResponseEntity.status(HttpStatus.OK).body(kycDetails);
    }




}
