package com.zenjob.challenge.exceptionhandler;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int statusCode;
    private final String message;
}
