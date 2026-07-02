package com.dessirestore.service_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // <-- IMPORTANTE
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
        return http
                // 1. ESTO ARREGLA EL 403: Le dice a Spring Security que no bloquee el POST por venir del Gateway
                .cors(Customizer.withDefaults()) 
                
                // 2. Desactivar CSRF (Obligatorio en APIs REST)
                .csrf(csrf -> csrf.disable())
                
                // 3. Nuestra lista VIP intocable
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/auth/**",               
                        "/v3/api-docs/**",        
                        "/swagger-ui/**",         
                        "/swagger-ui.html",
                        "/error"        
                    ).permitAll()
                    .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}