package com.cristianmanuel.Kinalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Rutas publicas: no requieren autenticacion
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/registro").permitAll()

                        // Gestion de usuarios: solo ADMIN puede crear, editar o eliminar
                        .requestMatchers("/usuarios/web/nuevo", "/usuarios/web/guardar",
                                "/usuarios/web/editar/**", "/usuarios/web/actualizar/**",
                                "/usuarios/web/eliminar/**").hasRole("ADMIN")

                        // Gestion de clientes: solo ADMIN puede crear, editar o eliminar
                        .requestMatchers("/clientes/web/nuevo", "/clientes/web/guardar",
                                "/clientes/web/editar/**", "/clientes/web/actualizar/**",
                                "/clientes/web/eliminar/**").hasRole("ADMIN")

                        // Gestion de productos: solo ADMIN puede crear, editar o eliminar
                        .requestMatchers("/productos/web/nuevo", "/productos/web/guardar",
                                "/productos/web/editar/**", "/productos/web/actualizar/**",
                                "/productos/web/eliminar/**").hasRole("ADMIN")

                        // Gestion de ventas: solo ADMIN puede crear, editar o eliminar
                        .requestMatchers("/ventas/web/nuevo", "/ventas/web/guardar",
                                "/ventas/web/editar/**", "/ventas/web/actualizar/**",
                                "/ventas/web/eliminar/**").hasRole("ADMIN")

                        // Gestion de detalle de venta: solo ADMIN puede crear, editar o eliminar
                        .requestMatchers("/detalleventa/web/nuevo", "/detalleventa/web/guardar",
                                "/detalleventa/web/editar/**", "/detalleventa/web/actualizar/**",
                                "/detalleventa/web/eliminar/**").hasRole("ADMIN")

                        // Cualquier otra ruta requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/web/menu", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Encoder sin cifrado, solo para desarrollo
        return NoOpPasswordEncoder.getInstance();
    }
}