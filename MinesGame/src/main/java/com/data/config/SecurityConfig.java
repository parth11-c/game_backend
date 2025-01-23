package com.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.data.Jwt.JwtAuth;
import com.data.Jwt.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuth point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private UserDetailsService detailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
//
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeRequests(auth -> auth
                // Permit access to public endpoints
                .requestMatchers("/auth/login","/api/{roomCode}/leaderboard","/getallusers","/api/admin/create-room","/api/{roomCode}/game-cashouts","/rooms/{roomCode}/games/{gameId}/cashout","/api/join-room","/rooms/{roomCode}/games/start","/rooms/{roomCode}/games/move", "/auth/register", "/auth/", "/").permitAll()
                // Define role-based access
                .requestMatchers("/api/owner/**").hasRole("OWNER")
                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "OWNER")
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .anyRequest().authenticated() // Authenticate any other requests
            )
            // Handle unauthorized access
            .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
            // Use stateless sessions
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT authentication filter
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
