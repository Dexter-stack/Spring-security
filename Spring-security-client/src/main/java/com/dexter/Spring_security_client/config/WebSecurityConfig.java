package com.dexter.Spring_security_client.config;


import com.dexter.Spring_security_client.config.jwtConfig.JwtAuthenticationFilter;
import com.dexter.Spring_security_client.serviceImpl.UserServiceImpl;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class WebSecurityConfig {



    private static final String [] WHITE_LIST_URLS ={
            "/hello",
            "/register",
            "/verifyRegistration",
            "/resendVerifyToken",
            "/resetPassword",
            "/savePassword",
            "/changePassword",
            "/login"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvide;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Apply default CORS configuration
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeRequests(authorize -> {
                            try {
                                authorize
                                        .requestMatchers(WHITE_LIST_URLS).permitAll() // Public access URLs
                                        .anyRequest().authenticated() // Any other request must be authenticated
                                        .and()
                                        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                        .authenticationProvider(authenticationProvide)
                                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                );


        return http.build();
    }




}
