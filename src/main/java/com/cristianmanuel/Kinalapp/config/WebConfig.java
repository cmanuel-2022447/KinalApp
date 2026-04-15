package com.cristianmanuel.Kinalapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registra el LoginInterceptor para proteger todas las rutas
 * excepto /login, /registro y los recursos estáticos.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // Rutas protegidas:
                .addPathPatterns("/web/**", "/clientes/**", "/productos/**",
                                 "/usuarios/**", "/ventas/**", "/detalleventa/**")
                // Rutas públicas (excluidas):
                .excludePathPatterns("/login", "/registro", "/css/**", "/js/**");
    }
}
