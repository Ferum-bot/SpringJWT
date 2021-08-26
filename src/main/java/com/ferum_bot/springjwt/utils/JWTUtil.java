package com.ferum_bot.springjwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.records.JWTContainer;
import com.ferum_bot.springjwt.models.records.JWTUserData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

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

        var accessToken = createAccessToken(
            user.getUsername(), new Date(System.currentTimeMillis() + accessTokenDuration), userRoles
        );

        var refreshToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenDuration))
            .withIssuer(ISSUER_ALIAS)
            .sign(signAlgorithm);

        return new JWTContainer(accessToken, refreshToken);
    }

    static public String getAccessTokenFromRefresh(String refreshToken, com.ferum_bot.springjwt.models.entities.User user) {
        var decodedJWT = getDecodedToken(refreshToken);

        var refreshExpiresAt = decodedJWT.getExpiresAt();
        var expectedAccessExpiresAt = new Date(System.currentTimeMillis() + accessTokenDuration);
        Date actualAccessExpiresAt;

        if (refreshExpiresAt.getTime() < expectedAccessExpiresAt.getTime()) {
            actualAccessExpiresAt = refreshExpiresAt;
        } else {
            actualAccessExpiresAt = expectedAccessExpiresAt;
        }

        var userNickname = user.getNickname();
        var userRoles = user.getRoles().stream().map(Role::getName).toList();
        return createAccessToken(userNickname, actualAccessExpiresAt, userRoles);
    }

    static public JWTUserData getUserDataFromAccessToken(String accessToken) {
        var decodedJWT = getDecodedToken(accessToken);
        var userNickname = decodedJWT.getSubject();
        var userRoles = decodedJWT.getClaim(ROLES_ALIAS).asList(String.class);
        return new JWTUserData(userNickname, userRoles);
    }

    static public String getUserNickFromRefreshToken(String refreshToken) {
        return getDecodedToken(refreshToken).getSubject();
    }

    static private DecodedJWT getDecodedToken(String token) {
        var verifier = JWT.require(signAlgorithm).build();
        return verifier.verify(token);
    }

    static private String createAccessToken(
        String nickname, Date expiresAt, List<String> roles
    ) {
        return JWT.create()
                .withSubject(nickname)
                .withExpiresAt(expiresAt)
                .withIssuer(ISSUER_ALIAS)
                .withClaim(ROLES_ALIAS, roles)
                .sign(signAlgorithm);
    }
}
