package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador CRUD para la entidad Usuario.
 * Nota importante: La eliminación está deshabilitada por política del sistema
 * (integridad de datos y auditoría). Solo se puede desactivar (cambiar estado).
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ========== REST API ==========

    /**
     * Listar todos los usuarios (GET /usuarios)
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Usuario>> listarRest() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Buscar usuario por código (GET /usuarios/{codigo})
     */
    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable Long codigo) {
        return usuarioService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Filtrar usuarios por estado (activo/inactivo).
     * GET /usuarios/estado/{estado}  donde estado es 1 (activo) o 0 (inactivo)
     */
    @GetMapping("/estado/{estado}")
    @ResponseBody
    public ResponseEntity<List<Usuario>> buscarPorEstado(@PathVariable int estado) {
        List<Usuario> usuarios = usuarioService.buscarPorEstado(estado);
        return usuarios.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(usuarios);
    }

    /**
     * Crear nuevo usuario (POST /usuarios)
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> guardarRest(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(usuarioService.guardar(usuario), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Eliminar usuario (DELETE /usuarios/{codigo})
     * Este endpoint REST sí permite eliminación física (para APIs externas).
     * La interfaz web no lo usa.
     */
    @DeleteMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Void> eliminarRest(@PathVariable Long codigo) {
        if (!usuarioService.existePorCodigo(codigo)) return ResponseEntity.notFound().build();
        usuarioService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualizar usuario (PUT /usuarios/{codigo})
     */
    @PutMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<?> actualizarRest(@PathVariable Long codigo, @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(usuarioService.actualizar(codigo, usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ========== Vistas Web ==========

    /**
     * Lista de usuarios en HTML (GET /usuarios/web)
     */
    @GetMapping("/web")
    public String listarWeb(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/usuarios";
    }

    /**
     * Formulario para nuevo usuario (GET /usuarios/web/nuevo)
     */
    @GetMapping("/web/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/usuarios";
    }

    /**
     * Guardar desde formulario web (POST /usuarios/web/guardar)
     */
    @PostMapping("/web/guardar")
    public String guardarWeb(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios/web";
    }

    /**
     * Formulario de edición (GET /usuarios/web/editar/{codigo})
     */
    @GetMapping("/web/editar/{codigo}")
    public String editarFormulario(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            model.addAttribute("usuario", usuario);
            return "usuarios/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios/web";
        }
    }

    /**
     * Actualizar desde formulario web (POST /usuarios/web/actualizar/{codigo})
     */
    @PostMapping("/web/actualizar/{codigo}")
    public String actualizarWeb(@PathVariable Long codigo, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizar(codigo, usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios/web";
    }

    /**
     * Eliminación deshabilitada en web por política del sistema.
     * En lugar de borrar, se debería cambiar el estado a inactivo.
     * Este método muestra un mensaje de error explicativo.
     */
    @GetMapping("/web/eliminar/{codigo}")
    public String eliminarWeb(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "❌ No se permite eliminar usuarios. Los registros primarios no pueden ser borrados.");
        return "redirect:/usuarios/web";
    }
}