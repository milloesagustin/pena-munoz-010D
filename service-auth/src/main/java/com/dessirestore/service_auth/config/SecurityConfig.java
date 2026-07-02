package com.dessirestore.service_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    // --- LISTA VIP (Sin Token) ---
                    .requestMatchers(
                        "/auth/**",               // Permite tus endpoints de login, registro y el JSON de Swagger
                        "/v3/api-docs/**",        // Permite configuraciones internas de OpenAPI
                        "/swagger-ui/**",         // Permite los colores, javascript y botones de Swagger
                        "/swagger-ui.html"        // Permite la página web base
                    ).permitAll()
                    
                    .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}