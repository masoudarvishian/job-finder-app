package com.zenjob.challenge.rest.exceptionhandler;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int statusCode;
    private final String message;
}
