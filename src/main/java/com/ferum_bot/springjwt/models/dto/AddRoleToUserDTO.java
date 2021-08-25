package com.ferum_bot.springjwt.models.dto;

public record AddRoleToUserDTO(
    String userNickname,
    String roleName
) {  }
