package com.cristianmanuel.Kinalapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Inyecta el URI de la petición actual en todos los modelos
 * para que el layout pueda resaltar el enlace activo en la navbar.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
