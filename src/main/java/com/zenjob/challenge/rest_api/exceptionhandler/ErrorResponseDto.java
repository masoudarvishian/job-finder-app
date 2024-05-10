package com.zenjob.challenge.rest_api.exceptionhandler;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int statusCode;
    private final String message;
}
