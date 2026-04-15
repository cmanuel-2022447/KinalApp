package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador encargado de la autenticación de usuarios.
 * Maneja el formulario de login, la validación de credenciales y el cierre de sesión.
 * Todas las rutas aquí son públicas (no requieren sesión) porque el interceptor las excluye.
 */
@Controller  // Indica que esta clase es un controlador Spring MVC, capaz de recibir peticiones HTTP.
public class LoginController {

    // Dependencia del servicio de usuarios. Se inyecta por constructor (buena práctica).
    private final IUsuarioService usuarioService;

    // Constructor: Spring inyecta automáticamente el bean IUsuarioService.
    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Se ejecuta cuando el usuario hace GET a /login.
     * @return el nombre de la vista "login" (login.html en templates/)
     */
    @GetMapping("/login")  // Asocia este método a peticiones HTTP GET en la ruta /login
    public String mostrarLogin() {
        return "login";  // Spring buscará login.html en src/main/resources/templates/
    }

    /**
     * Procesa el envío del formulario de login.
     * Recibe username y password desde el formulario (POST /login).
     * @param username nombre de usuario enviado en el campo "username"
     * @param password contraseña enviada en el campo "password"
     * @param session objeto HttpSession para almacenar al usuario autenticado
     * @param redirectAttributes para enviar mensajes flash (solo duran una redirección)
     * @return redirección a /web/menu si éxito, o a /login con mensaje de error
     */
    @PostMapping("/login")  // Maneja POST en /login
    public String procesarLogin(@RequestParam String username,   // @RequestParam extrae el parámetro del formulario
                                @RequestParam String password,
                                HttpSession session,              // Spring inyecta la sesión HTTP actual
                                RedirectAttributes redirectAttributes) {

        // Llamada al servicio para validar credenciales. Retorna Optional<Usuario>.
        Optional<Usuario> usuarioOpt = usuarioService.login(username, password);

        // Verificamos si el usuario existe y la contraseña es correcta.
        if (usuarioOpt.isPresent()) {
            // Guardamos el objeto Usuario completo en la sesión.
            // El interceptor LoginInterceptor revisará este atributo para permitir o denegar el acceso.
            session.setAttribute("usuarioLogueado", usuarioOpt.get());
            // Redirige al menú principal (ruta protegida por el interceptor)
            return "redirect:/web/menu";
        } else {
            // Si falla la autenticación, agregamos un mensaje flash de error.
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
            // Redirige de nuevo al formulario de login.
            return "redirect:/login";
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     * Se accede vía GET a /logout.
     * @param session la sesión HTTP actual
     * @param redirectAttributes para mensaje flash de éxito
     * @return redirección a /login con mensaje
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();  // Destruye la sesión, eliminando todos sus atributos.
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada correctamente.");
        return "redirect:/login";
    }
}
