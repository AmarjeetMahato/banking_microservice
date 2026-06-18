package com.payment_service.domain.Transaction.services;

import com.payment_service.domain.Transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements  TransactionService {

    private  final TransactionRepository transactionRepository;

}
