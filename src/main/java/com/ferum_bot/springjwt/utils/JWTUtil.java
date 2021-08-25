package com.ferum_bot.springjwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTUtil {

    public record JWTContainer(
        String accessToken,
        String refreshToken
    ) {  }

    private static String securityWord = "Very Secret word!";

    private static Long accessTokenDuration = 10 * 60 * 1000L;

    private static Long refreshTokenDuration = 30 * 60 * 1000L;

    static public JWTContainer getTokens(User user) {
        var algorithm = Algorithm.HMAC256(securityWord.getBytes(StandardCharsets.UTF_8));

        var userRoles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenDuration))
                .withIssuer("SpringJWT Application")
                .withClaim("Roles", userRoles)
                .sign(algorithm);

        var refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenDuration))
                .withIssuer("SpringJWT Application")
                .sign(algorithm);

        return new JWTContainer(accessToken, refreshToken);
    }

}
