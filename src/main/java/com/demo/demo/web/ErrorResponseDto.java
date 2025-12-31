package com.demo.demo.web;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime time,
        String message,
        String detailedMessage
) {

}
