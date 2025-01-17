package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/users/sign-up",
                    "/api/users/login",
                    "/h2-console/**",
                    "/",
                    "/swagger-ui/**",    // Permitir acceso a Swagger-UI
                    "/v3/api-docs/**",   // Permitir acceso a la especificación OpenAPI 3
                    "/swagger-resources/**", // Requerido por Springfox si usas Swagger 2
                    "/webjars/**"        // Recursos estáticos de Swagger-UI
                    ).permitAll() // Permitimos sign-up, login y H2 console sin autenticación
            .anyRequest().authenticated() // Requiere autenticación para todos los demás endpoints
            .and()
            .headers()
            .frameOptions().sameOrigin(); // Permite H2 Console

        return http.build();
    }
}
