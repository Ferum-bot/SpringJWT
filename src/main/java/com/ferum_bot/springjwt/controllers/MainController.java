package com.ferum_bot.springjwt.controllers;

import com.ferum_bot.springjwt.models.dto.AddRoleToUserDTO;
import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.entities.User;
import com.ferum_bot.springjwt.services.MainService;
import com.ferum_bot.springjwt.utils.JWTUtil;
import com.ferum_bot.springjwt.utils.LocationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@RestController
@RequestMapping("/api")
public class MainController {

    private final MainService service;

    @Autowired
    public MainController(MainService service) {
        this.service = service;
    }

    @GetMapping("/users/all")
    public ResponseEntity<Collection<User>> getAllUsers() {
        var users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(
        @RequestBody User user,
        HttpServletRequest request
    ) {
        var savedUser = service.saveUser(user);
        var location = LocationUtils.provideSaveUserLocation(request);
        return ResponseEntity.created(location).body(savedUser);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(
        @RequestBody Role role,
        HttpServletRequest request
    ) {
        var savedRole = service.saveRole(role);
        var location = LocationUtils.provideSaveRoleLocation(request);
        return ResponseEntity.created(location).body(savedRole);
    }

    @PostMapping("/user/addRole")
    public ResponseEntity<User> addRoleToUser(
        @RequestBody AddRoleToUserDTO addForm
    ) {
        var updatedUser = service.addRoleToUser(addForm.userNickname(), addForm.roleName());
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(
        @RequestHeader("Refresh-Token")
        String refreshToken,
        HttpServletRequest request
    ) {
        var userNickname = JWTUtil.getUserNickFromRefreshToken(refreshToken);
        var user = service.getUser(userNickname);
        var newAccessToken = JWTUtil.getAccessTokenFromRefresh(refreshToken, user);
        return ResponseEntity.ok()
                .header("Access-Token", newAccessToken)
                .header("Refresh-Token", refreshToken)
                .build();
    }
}
