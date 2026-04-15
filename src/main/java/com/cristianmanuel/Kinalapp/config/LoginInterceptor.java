package com.cristianmanuel.Kinalapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor que reemplaza Spring Security:
 * si el usuario no ha iniciado sesión y trata de acceder
 * a una ruta protegida, lo redirige al login.
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        boolean logueado = session != null && session.getAttribute("usuarioLogueado") != null;

        if (!logueado) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;  // detiene la cadena: no llega al controller
        }
        return true;  // deja pasar
    }
}