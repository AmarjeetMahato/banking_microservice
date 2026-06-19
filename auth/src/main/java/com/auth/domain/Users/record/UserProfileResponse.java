package com.auth.domain.Users.record;

import java.util.List;

public record UserProfileResponse(
        boolean authenticated,
        List<String> roles, // 👈 Flattened into a simple list of strings
        String name,
        String principal,
        String remoteAddress
) {
}
