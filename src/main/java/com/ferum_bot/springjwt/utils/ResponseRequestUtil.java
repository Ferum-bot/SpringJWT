package com.ferum_bot.springjwt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferum_bot.springjwt.models.records.JWTContainer;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ResponseRequestUtil {

    public static void fillResponseWithTokens(
        HttpServletResponse response,
        JWTContainer tokens
    ) {
        var accessToken = tokens.accessToken();
        var refreshToken = tokens.refreshToken();
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
    }

    public static void addValueToResponse(
        HttpServletResponse response, String alias, String value
    ) throws IOException {
        var resultMap = new HashMap<String, String>();
        resultMap.put(alias, value);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), resultMap);
    }

    public static void addValueToResponse(
        HttpServletResponse response, String alias, Integer value
    ) throws IOException {
        var resultMap = new HashMap<String, Integer>();
        resultMap.put(alias, value);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), resultMap);
    }

    @Nullable
    public static String getRequestAuthorizationToken(
        HttpServletRequest request
    ) {
        var rawValue = request.getHeader(AUTHORIZATION);
        if (!rawValue.startsWith("Bearer ")) {
            return null;
        }
        return rawValue.substring("Bearer ".length());
    }

    @Nullable
    public static String getRequestRefreshToken(
        HttpServletRequest request
    ) {
        return request.getHeader("Refresh-Token");
    }
}
