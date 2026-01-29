package com.todoapp.todoapp.config;

import com.todoapp.todoapp.authorizationFilter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthorizationFilter jwtTokenFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        http
//                // 1. Disable CSRF using the new lambda syntax
//                .csrf(AbstractHttpConfigurer::disable)
//
//                // 2. Configure authorization using requestMatchers instead of antMatchers
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/user/signup", "/user/login","/user/users","/users/123/orders").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // 3. Add your custom JWT filter
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());


        return http.build();    }
}
