package com.auth.domain.Tokens.services;

import com.auth.domain.Tokens.dtos.TokenCreateDto;
import com.auth.domain.Tokens.dtos.TokenResponseDto;
import com.auth.domain.Tokens.dtos.UpdateTokenDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Users.entity.User;

public interface TokenService {

    TokenResponseDto createToken(TokenCreateDto tokenDto);

    TokenResponseDto updateToken(String tokenId, UpdateTokenDto tokenDto);

    TokenResponseDto fetchById(String tokenId);

    Token validateEmailVerificationToken(String email, String tokenValue);

    void revokeToken(Token token);

    Token generateEmailVerificationToken(User user);

    Token createLoginVerificationToken(User user);
}
