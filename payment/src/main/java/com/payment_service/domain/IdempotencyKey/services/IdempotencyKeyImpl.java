package com.payment_service.domain.IdempotencyKey.services;

import com.payment_service.domain.IdempotencyKey.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyKeyImpl implements IdempotencyKey {
    private  final IdempotencyKeyRepository keyRepository;
}
