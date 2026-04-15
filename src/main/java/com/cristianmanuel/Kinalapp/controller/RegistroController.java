package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para el registro de nuevos usuarios.
 * Permite crear una cuenta sin necesidad de estar autenticado a cada rato.
 * Las validaciones incluyen unicidad de username y email.
 */
@Controller
@RequestMapping("/registro")  // Ruta base: /registro
public class RegistroController {

    private final IUsuarioService usuarioService;

    public RegistroController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra el formulario de registro.
     * GET /registro
     * @param modelo para pasar un objeto Usuario vacío al formulario
     * @return vista "registro"
     */
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    /**
     * Procesa el envío del formulario de registro.
     * POST /registro
     * @param usuario objeto bindeado desde el formulario (username, password, email, etc.)
     * @param redirectAttributes para mensajes flash
     * @return redirección a login si éxito, o de vuelta a registro si error
     */
    @PostMapping
    public String registrar(@ModelAttribute Usuario usuario,
                            RedirectAttributes redirectAttributes) {

        // Validación: ¿Ya existe un usuario con el mismo username?
        if (usuarioService.existeUsername(usuario.getUsername())) {
            redirectAttributes.addFlashAttribute("error",
                    "El nombre de usuario \"" + usuario.getUsername() + "\" ya está en uso. Elige otro.");
            return "redirect:/registro";
        }

        // Validación: ¿Ya existe un usuario con el mismo email?
        if (usuarioService.existeEmail(usuario.getEmail())) {
            redirectAttributes.addFlashAttribute("error",
                    "El correo \"" + usuario.getEmail() + "\" ya está registrado.");
            return "redirect:/registro";
        }

        // Intentamos guardar. El servicio puede lanzar IllegalArgumentException por otras reglas.
        try {
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("success",
                    "✅ Cuenta creada exitosamente. ¡Ya puedes iniciar sesión!");
            return "redirect:/login";  // Redirige al login para que el nuevo usuario entre
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }
}
