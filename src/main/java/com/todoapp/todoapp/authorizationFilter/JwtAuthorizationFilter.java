package com.todoapp.todoapp.authorizationFilter;

import com.todoapp.todoapp.Entities.Users;
import com.todoapp.todoapp.service.UsersService;
import com.todoapp.todoapp.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Lazy; // Add this import


import java.io.IOException;

@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UsersService userService;

    public JwtAuthorizationFilter(@Lazy UsersService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("authenticating request");
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = JwtUtil.getUsernameFromToken(token);
            Users user = userService.findByUsername(username);
            if (user != null && JwtUtil.validateToken(token, username)) {
                // Set the user in the security context
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex){
            log.error("error in filter",ex);
        }
    }
}
