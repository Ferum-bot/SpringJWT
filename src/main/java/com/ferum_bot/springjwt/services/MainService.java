package com.ferum_bot.springjwt.services;

import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.entities.User;

import java.util.Collection;

public interface MainService {

    User saveUser(User user);

    User getUser(String userNickname);

    Collection<User> getAllUsers();

    Role saveRole(Role role);

    User addRoleToUser(String userNickname, String roleName);

}
