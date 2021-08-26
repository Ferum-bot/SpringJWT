package com.ferum_bot.springjwt.models.records;

import java.util.Collection;

public record JWTUserData(
    String userNickname,
    Collection<String> userRoles
) {  }