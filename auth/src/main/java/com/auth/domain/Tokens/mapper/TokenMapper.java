package com.auth.domain.Tokens.mapper;

import com.auth.domain.Tokens.dtos.TokenCreateDto;
import com.auth.domain.Tokens.dtos.TokenResponseDto;
import com.auth.domain.Tokens.dtos.UpdateTokenDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Users.entity.User;
import com.auth.globalException.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {

    // 1. Create DTO ko Token Entity me convert karne ke liye
    public Token toEntity(TokenCreateDto dto) {
        if (dto == null) {
            throw new BadRequestException("Token creation data cannot be null");
        }

        // User object ka reference create kar rahe hain id ke saath
        User user = new User();
        user.setId(dto.getUserId());

        return Token.builder()
                .tokenType(dto.getTokenType())
                .user(user)
                .revoked(false) // Naya token default me active hoga
                .expired(false) // Naya token default me active hoga
                // Note: tokenValue aur expiryDate aap generally service layer me generate karenge
                .build();
    }

    // 2. Existing Token Entity ko Update DTO ke sath modify karne ke liye
    public void updateEntityFromDto(UpdateTokenDto dto, Token token) {
        if (dto == null || token == null) {
            throw new BadRequestException("Update data or target token cannot be null");
        }

        token.setRevoked(dto.isRevoked());
        token.setExpired(dto.isExpired());
    }

    // 3. Token Entity ko Response DTO me convert karne ke liye
    public TokenResponseDto toResponseDto(Token token) {
        if (token == null) {
            return null;
        }

        return TokenResponseDto.builder()
                .id(token.getId()) // Agar BaseEntity me id field hai
                .tokenValue(token.getTokenValue())
                .tokenType(token.getTokenType())
                .revoked(token.isRevoked())
                .expired(token.isExpired())
                .expiryDate(token.getExpiryDate())
                .userId(token.getUser() != null ? token.getUser().getId() : null) // Null pointer safety ke saath
                .build();
    }
}