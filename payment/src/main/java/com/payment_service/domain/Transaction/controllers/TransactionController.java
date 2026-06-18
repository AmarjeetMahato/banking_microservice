package com.payment_service.domain.Transaction.controllers;


import com.payment_service.domain.Transaction.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private  final TransactionService transactionService;


}
