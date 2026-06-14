package com.kyc_service.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
    private String message;
    private boolean success;
    private HttpStatus status;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
}
