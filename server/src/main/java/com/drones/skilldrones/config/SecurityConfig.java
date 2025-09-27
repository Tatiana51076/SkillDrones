package com.drones.skilldrones.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    // Белый список URL-адресов, которые должны быть общедоступными
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**", // Обратите внимание на "/**" для включения всех путей:cite[3]
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**", // Важно для доступа к интерфейсу Swagger UI:cite[5]
            "/webjars/**",
            "/api/swagger-ui.html", // Явно разрешите ваш полный путь
            "/api/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll() // Разрешить доступ к Swagger без аутентификации
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .csrf(AbstractHttpConfigurer::disable) // Отключить CSRF защиту для API:cite[1]
                .httpBasic(withDefaults()); // (Опционально) Можно включить базовую аутентификацию для других эндпоинтов

        return http.build();
    }
}
