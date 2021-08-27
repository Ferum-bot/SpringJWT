package com.ferum_bot.springjwt.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.records.JWTContainer;
import com.ferum_bot.springjwt.models.records.JWTUserData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class HMAC384JWTTokensComponent implements JWTTokensComponent {

    private static final String ROLES_ALIAS = "Roles";
    private static final String ISSUER_ALIAS = "SpringJWT Application";

    @Value("${api.token.security-word}")
    private String secretWord;

    @Value("${api.token.access.duration}")
    private Long accessTokenDuration;

    @Value("${api.token.refresh.duration}")
    private Long refreshTokenDuration;

    @Override
    public JWTContainer getTokens(User user) {
        var signAlgorithm = getSignAlgorithm();
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

    @Override
    public String getAccessTokenFromRefresh(
        String refreshToken, com.ferum_bot.springjwt.models.entities.User user
    ) {
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

    @Override
    public JWTUserData getUserDataFromAccessToken(String accessToken) {
        var decodedJWT = getDecodedToken(accessToken);
        var userNickname = decodedJWT.getSubject();
        var userRoles = decodedJWT.getClaim(ROLES_ALIAS).asList(String.class);
        return new JWTUserData(userNickname, userRoles);
    }

    @Override
    public String getUserNickFromRefreshToken(String refreshToken) {
        return getDecodedToken(refreshToken).getSubject();
    }

    private Algorithm getSignAlgorithm() {
        return Algorithm.HMAC384(secretWord.getBytes(StandardCharsets.UTF_8));
    }

    private DecodedJWT getDecodedToken(String token) {
        var signAlgorithm = getSignAlgorithm();
        var verifier = JWT.require(signAlgorithm).build();
        return verifier.verify(token);
    }

    private String createAccessToken(
        String nickname, Date expiresAt, List<String> roles
    ) {
        var signAlgorithm = getSignAlgorithm();
        return JWT.create()
                .withSubject(nickname)
                .withExpiresAt(expiresAt)
                .withIssuer(ISSUER_ALIAS)
                .withClaim(ROLES_ALIAS, roles)
                .sign(signAlgorithm);
    }
}
