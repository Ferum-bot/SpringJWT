package com.ferum_bot.springjwt.configurations;

import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.entities.User;
import com.ferum_bot.springjwt.services.MainService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDataBaseConfig {

    @Bean
    public CommandLineRunner initDatabase(MainService service) {
        return args -> {

            var superAdminRole = new Role("SUPER_ADMIN");
            var adminRole = new Role("ADMIN");
            var userRole = new Role("USER");

            service.saveRole(superAdminRole);
            service.saveRole(adminRole);
            service.saveRole(userRole);

            var superAdminUser = new User("Matvey Popov", "Ferum-bot", "1234");
            var adminUser = new User("Not Matvey", "Pussy-Destroyer", "1111");
            var firstUser = new User("First User", "user1", "2222");
            var secondUser = new User("Second User", "user2", "3333");
            var thirdUser = new User("Third User", "user3", "4444");

            service.saveUser(superAdminUser);
            service.saveUser(adminUser);
            service.saveUser(firstUser);
            service.saveUser(secondUser);
            service.saveUser(thirdUser);

            service.addRoleToUser(superAdminUser.getNickname(), superAdminRole.getName());
            service.addRoleToUser(superAdminUser.getNickname(), adminRole.getName());
            service.addRoleToUser(superAdminUser.getNickname(), userRole.getName());
            service.addRoleToUser(adminUser.getNickname(), adminRole.getName());
            service.addRoleToUser(adminUser.getNickname(), userRole.getName());
            service.addRoleToUser(firstUser.getNickname(), userRole.getName());
            service.addRoleToUser(secondUser.getNickname(), userRole.getName());
            service.addRoleToUser(thirdUser.getNickname(), userRole.getName());
        };
    }

}
