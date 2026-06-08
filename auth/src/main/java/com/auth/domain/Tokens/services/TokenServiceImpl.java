package com.auth.domain.Tokens.services;

import com.auth.domain.Tokens.dtos.TokenCreateDto;
import com.auth.domain.Tokens.dtos.TokenResponseDto;
import com.auth.domain.Tokens.dtos.UpdateTokenDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.enums.TokenType;
import com.auth.domain.Tokens.mapper.TokenMapper;
import com.auth.domain.Tokens.repository.TokenRepository;
import com.auth.globalException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.IToken;
import org.aspectj.weaver.patterns.Pointcut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

     private final  TokenRepository tokenRepository;
     private  final TokenMapper tokenMapper;

    @Override
    @Transactional
    public TokenResponseDto createToken(TokenCreateDto tokenDto) {
        log.info("Initiating 6-digit OTP generation for User ID: {}", tokenDto.getUserId());

        Token token = tokenMapper.toEntity(tokenDto);

        // 1. Production-grade Secure 6-digit OTP generation
        // SecureRandom use kar rahe hain standard Random ki jagah cryptographically strong hone ke liye
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        int otpNum = 100000 + secureRandom.nextInt(900000); // Generates range 100000 to 999999
        String secureOtp = String.valueOf(otpNum);

        token.setTokenValue(secureOtp);
        token.setExpiryDate(calculateExpiryDate(tokenDto.getTokenType()));

        Token savedToken = tokenRepository.save(token);
        log.info("Successfully generated and saved OTP token for User ID: {}", tokenDto.getUserId());

        return tokenMapper.toResponseDto(savedToken);
    }

    @Override
    @Transactional
    public TokenResponseDto updateToken(String tokenId, UpdateTokenDto tokenDto) { // Typo fixed to TokenUpdateDto
        log.info("Request received to update token ID: {}", tokenId);

        // 1. Existing token fetch kiya, agar nahi mila toh custom exception wrapper throw kiya
        Token existingToken = tokenRepository.findById(tokenId)
                .orElseThrow(() -> {
                    log.error("Token update failed. Token ID: {} not found", tokenId);
                    return new ResourceNotFoundException("Token not found with ID: " + tokenId);
                });

        // 2. Mapper ka use karke entity ko update kiya (revoked/expired flags)
        tokenMapper.updateEntityFromDto(tokenDto, existingToken);

        // 3. Save execution (@Transactional handles dirty checking, but explicit save is cleaner for logging)
        Token updatedToken = tokenRepository.save(existingToken);
        log.info("Token ID: {} successfully updated. Revoked: {}, Expired: {}",
                tokenId, updatedToken.isRevoked(), updatedToken.isExpired());

        return tokenMapper.toResponseDto(updatedToken);
    }

    @Override
    @Transactional(readOnly = true) // Performance optimization for fetch read-only queries
    public TokenResponseDto fetchById(String tokenId) {
        log.info("Fetching token details for ID/Value: {}", tokenId);

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

        /* // ALternate: Agar aap token_value string se search kar rahe hain, toh repository me findByTokenValue bana kar ye use karein:
        Token token = tokenRepository.findByTokenValue(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with value: " + tokenId));
        */

        return tokenMapper.toResponseDto(token);
    }

    // Helper method to set dynamic expiration time based on token type
    private LocalDateTime calculateExpiryDate(TokenType tokenType) {
        return switch (tokenType) {
            case EMAIL_VERIFICATION, PASSWORD_RESET -> LocalDateTime.now().plusMinutes(15); // 15 Minutes expiry
            case REFRESH -> LocalDateTime.now().plusDays(7); // 7 Days expiry
            default -> LocalDateTime.now().plusHours(1); // Default 1 hour
        };
    }
}
