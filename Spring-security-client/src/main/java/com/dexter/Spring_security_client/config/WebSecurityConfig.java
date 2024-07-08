package com.dexter.Spring_security_client.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {
    private static final String [] WHITE_LIST_URLS ={
            "/hello",
            "/register",
            "/verifyRegistration",
            "/resendVerifyToken"
    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(withDefaults()) // Use withDefaults() to apply the default CORS configuration
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST_URLS).permitAll() // Public access
                        .anyRequest().authenticated() // Any other request must be authenticated
                );

        return httpSecurity.build();
    }


}
