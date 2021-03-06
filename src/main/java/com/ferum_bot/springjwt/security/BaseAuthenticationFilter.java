package com.ferum_bot.springjwt.security;

import com.ferum_bot.springjwt.utils.ResponseRequestUtil;
import com.ferum_bot.springjwt.utils.jwt.JWTTokensComponent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTTokensComponent jwtTokensComponent;

    public BaseAuthenticationFilter(
            AuthenticationManager authenticationManager, JWTTokensComponent jwtTokensComponent
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokensComponent = jwtTokensComponent;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        var userNickname = request.getParameter("nickname");
        var userPassword = request.getParameter("password");
        var authenticationToken = new UsernamePasswordAuthenticationToken(userNickname, userPassword);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication
    ) throws IOException, ServletException {
        var user = (User) authentication.getPrincipal();
        var tokens = jwtTokensComponent.getTokens(user);

        ResponseRequestUtil.fillResponseWithTokens(response, tokens);
    }


}
