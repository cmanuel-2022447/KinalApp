package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Ventas;
import com.cristianmanuel.Kinalapp.service.IClienteService;
import com.cristianmanuel.Kinalapp.service.IUsuarioService;
import com.cristianmanuel.Kinalapp.service.IVentasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para la gestión de ventas.
 * Depende de tres servicios: Ventas, Cliente y Usuario.
 * Los servicios de Cliente y Usuario se usan para poblar los desplegables en los formularios.
 */
@Controller
@RequestMapping("/ventas")
public class VentasController {

    private final IVentasService ventasService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    // Inyección de todas las dependencias por constructor.
    public VentasController(IVentasService ventasService,
                            IClienteService clienteService,
                            IUsuarioService usuarioService) {
        this.ventasService   = ventasService;
        this.clienteService  = clienteService;
        this.usuarioService  = usuarioService;
    }

    // ========== REST API ==========

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Ventas>> listarRest() {
        return ResponseEntity.ok(ventasService.listarTodos());
    }

    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Ventas> buscarPorCodigo(@PathVariable Long codigo) {
        return ventasService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @ResponseBody
    public ResponseEntity<List<Ventas>> buscarPorEstado(@PathVariable int estado) {
        List<Ventas> ventas = ventasService.buscarPorEstado(estado);
        return ventas.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ventas);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> guardarRest(@RequestBody Ventas ventas) {
        try {
            return new ResponseEntity<>(ventasService.guardar(ventas), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Void> eliminarRest(@PathVariable Long codigo) {
        if (!ventasService.existePorCodigo(codigo)) return ResponseEntity.notFound().build();
        ventasService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<?> actualizarRest(@PathVariable Long codigo, @RequestBody Ventas ventas) {
        try {
            if (!ventasService.existePorCodigo(codigo)) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(ventasService.actualizar(codigo, ventas));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ========== Vistas Web ==========

    /**
     * Muestra la lista de ventas.
     */
    @GetMapping("/web")
    public String listarWeb(Model model) {
        model.addAttribute("ventas", ventasService.listarTodos());
        return "ventas/ventas";
    }

    /**
     * Formulario para nueva venta.
     * Se pasan al modelo: un objeto Ventas vacío, la lista de clientes y la lista de usuarios.
     * Esto permite seleccionar cliente y usuario desde desplegables.
     */
    @GetMapping("/web/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("venta", new Ventas());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "ventas/ventas";
    }

    /**
     * Guardar venta desde formulario web.
     */
    @PostMapping("/web/guardar")
    public String guardarWeb(@ModelAttribute Ventas venta, RedirectAttributes redirectAttributes) {
        try {
            ventasService.guardar(venta);
            redirectAttributes.addFlashAttribute("success", "Venta guardada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ventas/web";
    }

    /**
     * Formulario de edición de venta.
     * También carga las listas de clientes y usuarios.
     */
    @GetMapping("/web/editar/{codigo}")
    public String editarFormulario(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes) {
        try {
            Ventas venta = ventasService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "ventas/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ventas/web";
        }
    }

    /**
     * Actualizar venta desde formulario.
     */
    @PostMapping("/web/actualizar/{codigo}")
    public String actualizarWeb(@PathVariable Long codigo, @ModelAttribute Ventas venta, RedirectAttributes redirectAttributes) {
        try {
            ventasService.actualizar(codigo, venta);
            redirectAttributes.addFlashAttribute("success", "Venta actualizada correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ventas/web";
    }

    /**
     * Eliminar venta desde interfaz web.
     * Permite eliminación porque las ventas pueden anularse (pero en realidad debería ser un cambio de estado).
     */
    @GetMapping("/web/eliminar/{codigo}")
    public String eliminarWeb(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            ventasService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("success", "Venta eliminada correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ventas/web";
    }
}