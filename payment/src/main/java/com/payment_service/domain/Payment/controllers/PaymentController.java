package com.payment_service.domain.Payment.controllers;


import com.payment_service.domain.Payment.dtos.PaymentCreateRequestDto;
import com.payment_service.domain.Payment.dtos.PaymentResponseDto;
import com.payment_service.domain.Payment.dtos.PaymentStatusUpdateResponseDto;
import com.payment_service.domain.Payment.enums.PaymentStatus;
import com.payment_service.domain.Payment.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private  final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDto> createPaymentController(@Valid
                 @RequestBody PaymentCreateRequestDto dto){
        PaymentResponseDto responseDto= paymentService.createPayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{paymentId}/get_payment_byId")
    public ResponseEntity<PaymentResponseDto> getPaymentController(
            @Valid @PathVariable String paymentId){
        PaymentResponseDto responseDto= paymentService.getPaymentById(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{paymentId}/update_status")
    public  ResponseEntity<PaymentStatusUpdateResponseDto> updatePaymentStatus(
            @PathVariable String paymentId, @RequestBody PaymentStatus status ){
        PaymentStatusUpdateResponseDto responseDto = paymentService.updatePaymentStatus(paymentId,status);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/{paymentId}/delete")
    public ResponseEntity<?> deletePayment(@PathVariable String paymentId ){
         paymentService.deletePayment(paymentId);
         return  ResponseEntity.status(HttpStatus.NO_CONTENT).body("Payment Details successfully");
    }
}
