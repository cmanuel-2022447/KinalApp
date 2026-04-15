package com.cristianmanuel.Kinalapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Controlador para servir el menú principal de la aplicación.
 * Todas las rutas bajo /web están protegidas por el LoginInterceptor..
 * Si el usuario no ha iniciado sesión, será redirigido automáticamente al login.
 */
@Controller  // Marca esta clase como un bean de Spring MVC.
@RequestMapping("/web")  // Todas las rutas de este controlador empezarán con /web
public class MenuWebController {

    /**
     * Muestra el menú principal después de un login exitoso.
     * Se accede con GET /web/menu.
     * @return nombre de la vista "menu" (menu.html)
     */
    @GetMapping("/menu")  // Asocia este método a GET /web/menu
    public String menu() {
        return "menu";  // Vista que muestra opciones de navegación (clientes, productos, ventas, etc.)
    }
}
