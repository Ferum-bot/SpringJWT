package com.ferum_bot.springjwt.utils;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {

    public static void fillResponseWithTokens(
        HttpServletResponse response,
        JWTUtil.JWTContainer tokens
    ) {
        var accessToken = tokens.accessToken();
        var refreshToken = tokens.refreshToken();
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
    }

}
