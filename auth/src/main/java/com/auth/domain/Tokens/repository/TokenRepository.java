package com.auth.domain.Tokens.repository;

import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<Token,String> {
    Optional<Token> findByTokenValueAndTokenType(String tokenValue, TokenType tokenType);
}
