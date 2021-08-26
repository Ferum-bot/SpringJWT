package com.ferum_bot.springjwt.security;

import com.ferum_bot.springjwt.utils.JWTUtil;
import com.ferum_bot.springjwt.utils.ResponseRequestUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public class BaseAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getServletPath().startsWith("/api/refresh/token")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            doAuthorizationFilter(request, response, filterChain);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            response.setStatus(SC_FORBIDDEN);
            ResponseRequestUtil.addValueToResponse(response, "code", SC_FORBIDDEN);
            ResponseRequestUtil.addValueToResponse(response, "message", exception.getMessage());
        }
    }

    private void doAuthorizationFilter(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        var token = ResponseRequestUtil.getRequestAuthorizationToken(request);
        var userData = JWTUtil.getUserDataFromAccessToken(token);
        var userNickname = userData.userNickname();
        var userRoles = userData.userRoles();

        var authorities = new ArrayList<GrantedAuthority>();
        userRoles.forEach(role -> {
            var authority = new SimpleGrantedAuthority(role);
            authorities.add(authority);
        });

        var authenticationToken = new UsernamePasswordAuthenticationToken(userNickname, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
