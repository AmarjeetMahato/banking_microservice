package com.auth.domain.Tokens.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTokenDto {

    private boolean revoked;
    private boolean expired;
}
