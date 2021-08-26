package com.ferum_bot.springjwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ferum_bot.springjwt.models.records.JWTContainer;
import com.ferum_bot.springjwt.models.records.JWTUserData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

public class JWTUtil {

    private static final String ROLES_ALIAS = "Roles";
    private static final String ISSUER_ALIAS = "SpringJWT Application";

    private static final String securityWord = "Very Secret word!";

    private static final Algorithm signAlgorithm = Algorithm.HMAC256(securityWord.getBytes(StandardCharsets.UTF_8));

    private static final Long accessTokenDuration = 10 * 60 * 1000L;

    private static final Long refreshTokenDuration = 30 * 60 * 1000L;

    static public JWTContainer getTokens(User user) {
        var userRoles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenDuration))
                .withIssuer(ISSUER_ALIAS)
                .withClaim(ROLES_ALIAS, userRoles)
                .sign(signAlgorithm);

        var refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenDuration))
                .withIssuer(ISSUER_ALIAS)
                .sign(signAlgorithm);

        return new JWTContainer(accessToken, refreshToken);
    }

    static public JWTUserData getUserData(String accessToken) {
        var verifier = JWT.require(signAlgorithm).build();
        var decodedJWT = verifier.verify(accessToken);

        var userNickname = decodedJWT.getSubject();
        var userRoles = decodedJWT.getClaim(ROLES_ALIAS).asList(String.class);
        return new JWTUserData(userNickname, userRoles);
    }
}
