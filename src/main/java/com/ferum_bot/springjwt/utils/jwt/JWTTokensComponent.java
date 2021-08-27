package com.ferum_bot.springjwt.utils.jwt;

import com.ferum_bot.springjwt.models.records.JWTContainer;
import com.ferum_bot.springjwt.models.records.JWTUserData;
import org.springframework.security.core.userdetails.User;

public interface JWTTokensComponent {

    JWTContainer getTokens(User user);

    String getAccessTokenFromRefresh(String refreshToken, com.ferum_bot.springjwt.models.entities.User user);

    JWTUserData getUserDataFromAccessToken(String accessToken);

    String getUserNickFromRefreshToken(String refreshToken);

    Boolean tokenIsExpired(String accessToken);
}
