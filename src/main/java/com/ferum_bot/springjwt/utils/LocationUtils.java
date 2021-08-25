package com.ferum_bot.springjwt.utils;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class LocationUtils {

    public static URI provideSaveUserLocation(@NonNull  HttpServletRequest request) {
        return URI.create(
            ServletUriComponentsBuilder.fromContextPath(request).path("/api/user/save").toUriString()
        );
    }

    public static URI provideSaveRoleLocation(@NonNull  HttpServletRequest request) {
        return URI.create(
            ServletUriComponentsBuilder.fromContextPath(request).path("/api/role/save").toUriString()
        );
    }
}
