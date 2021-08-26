package com.ferum_bot.springjwt.models.records;

public record JWTContainer(
    String accessToken,
    String refreshToken
) {  }