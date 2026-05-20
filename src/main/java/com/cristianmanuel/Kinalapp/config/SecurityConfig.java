package com.cristianmanuel.Kinalapp.config;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Rutas publicas: no requieren autenticacion
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/registro").permitAll()

                        // Gestion de usuarios: ADMIN y USER pueden crear; solo ADMIN puede editar o eliminar
                        .requestMatchers("/usuarios/web/editar/**", "/usuarios/web/actualizar/**",
                                "/usuarios/web/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/web/nuevo", "/usuarios/web/guardar").hasAnyRole("ADMIN","USER")

                        // Gestion de clientes: ADMIN y USER pueden crear; solo ADMIN puede editar o eliminar
                        .requestMatchers("/clientes/web/editar/**", "/clientes/web/actualizar/**",
                                "/clientes/web/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/clientes/web/nuevo", "/clientes/web/guardar").hasAnyRole("ADMIN","USER")

                        // Gestion de productos: ADMIN y USER pueden crear; solo ADMIN puede editar o eliminar
                        .requestMatchers("/productos/web/editar/**", "/productos/web/actualizar/**",
                                "/productos/web/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/productos/web/nuevo", "/productos/web/guardar").hasAnyRole("ADMIN","USER")

                        // Gestion de ventas: ADMIN y USER pueden crear; solo ADMIN puede editar o eliminar
                        .requestMatchers("/ventas/web/editar/**", "/ventas/web/actualizar/**",
                                "/ventas/web/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/ventas/web/nuevo", "/ventas/web/guardar").hasAnyRole("ADMIN","USER")

                        // Gestion de detalle de venta: ADMIN y USER pueden crear; solo ADMIN puede editar o eliminar
                        .requestMatchers("/detalleventa/web/editar/**", "/detalleventa/web/actualizar/**",
                                "/detalleventa/web/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/detalleventa/web/nuevo", "/detalleventa/web/guardar").hasAnyRole("ADMIN","USER")

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
        // BCrypt: encriptación segura para contraseñas en producción
        return new BCryptPasswordEncoder();
    }
}
