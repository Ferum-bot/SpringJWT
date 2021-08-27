package com.ferum_bot.springjwt.models.dto;

public record ErrorResponse(
    Integer code,
    String statusMessage
) {
}
