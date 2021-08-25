package com.ferum_bot.springjwt.application;

import com.ferum_bot.springjwt.configurations.InitDataBaseConfig;
import com.ferum_bot.springjwt.controllers.MainController;
import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.entities.User;
import com.ferum_bot.springjwt.repositories.RoleRepository;
import com.ferum_bot.springjwt.repositories.UserRepository;
import com.ferum_bot.springjwt.security.SecurityBeans;
import com.ferum_bot.springjwt.security.SecurityConfig;
import com.ferum_bot.springjwt.services.MainServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Import(value = {
    InitDataBaseConfig.class,
    SecurityConfig.class,
    SecurityBeans.class,
    MainController.class,
    MainServiceImpl.class,
})
@EnableJpaRepositories(basePackageClasses = {
    RoleRepository.class,
    UserRepository.class
})
@EntityScan(basePackageClasses = {
    Role.class,
    User.class
})
public class SpringJwtApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(SpringJwtApplication.class, args);
    }

}
