package com.ferum_bot.springjwt.utils.jwt;

import com.ferum_bot.springjwt.models.records.JWTContainer;
import com.ferum_bot.springjwt.models.records.JWTUserData;
import org.springframework.security.core.userdetails.User;

public interface JWTTokensComponent {

    public JWTContainer getTokens(User user);

    public String getAccessTokenFromRefresh(String refreshToken, com.ferum_bot.springjwt.models.entities.User user);

    public JWTUserData getUserDataFromAccessToken(String accessToken);

    public String getUserNickFromRefreshToken(String refreshToken);
}
