package com.azurep1.p1be.security;

import lombok.SneakyThrows;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests().anyRequest().permitAll();
    }

}
