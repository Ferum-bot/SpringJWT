package com.ferum_bot.springjwt.utils.jwt;

import com.ferum_bot.springjwt.utils.jwt.HMAC384JWTTokensComponent;
import com.ferum_bot.springjwt.utils.jwt.JWTTokensComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Bean
    public JWTTokensComponent provideJWTTokensComponent() {
        return new HMAC384JWTTokensComponent();
    }

}
