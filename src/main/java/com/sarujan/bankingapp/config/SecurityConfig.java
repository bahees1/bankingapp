package com.sarujan.bankingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // For APIs + Postman testing (we'll do proper JWT CSRF strategy later)
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                                // allow auth endpoints without token
                                .requestMatchers("/api/auth/**").permitAll()

                                // allow error endpoint (helps during dev)
                                .requestMatchers("/error").permitAll()

                                // everything else: for now allow all OR require auth (choose one)
                                .anyRequest().permitAll()
                        // later we will switch to:
                        // .anyRequest().authenticated()
                )

                // Disable default login page / browser auth popups
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable());

        return http.build();
    }
}
