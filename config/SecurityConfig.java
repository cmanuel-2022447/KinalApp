package com.cristianmanuel.Kinalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @Configuration: indica que esta clase contiene configuraciones de Spring
@EnableWebSecurity
// @EnableWebSecurity: activa la seguridad web en el proyecto
public class SecurityConfig {

    @Bean
    // @Bean: Spring va a gestionar este objeto
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Desactivamos CSRF para poder usar Postman sin problemas
                .authorizeHttpRequests(auth -> auth
                        // Rutas publicas: cualquier persona puede acceder sin autenticarse
                        .requestMatchers("/login").permitAll()
                        // Rutas protegidas: solo ADMIN puede crear, actualizar y eliminar
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        // Rutas de consulta: cualquier usuario autenticado puede ver
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/**").hasAnyRole("ADMIN", "USER")
                        // Cualquier otra ruta requiere autenticacion
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {});
        // httpBasic: activa autenticacion basica (usuario y contraseña)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt: algoritmo seguro para encriptar contraseñas
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        // InMemory: los usuarios se guardan en memoria (sin BD)
        // Esto es lo mas basico, ideal para pruebas

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                // encode: encripta la contraseña con BCrypt
                .roles("ADMIN")
                // ADMIN: puede hacer GET, POST, PUT y DELETE
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                // USER: solo puede hacer GET
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}