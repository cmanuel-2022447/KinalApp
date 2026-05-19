package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.DetalleVenta;
import com.cristianmanuel.Kinalapp.service.IDetalleVentaService;
import com.cristianmanuel.Kinalapp.service.IProductoService;
import com.cristianmanuel.Kinalapp.service.IVentasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador que maneja las operaciones CRUD para DetalleVenta.
 * Proporciona endpoints REST (JSON) y rutas para vistas web (Thymeleaf/JSP).
 */
@Controller
@RequestMapping("/detalleventa")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;
    private final IProductoService     productoService;
    private final IVentasService       ventasService;

    // Inyección de dependencias por constructor (recomendada sobre @Autowired en campo)
    public DetalleVentaController(IDetalleVentaService detalleVentaService,
                                  IProductoService productoService,
                                  IVentasService ventasService) {
        this.detalleVentaService = detalleVentaService;
        this.productoService     = productoService;
        this.ventasService       = ventasService;
    }

    // ========== ENDPOINTS REST (API JSON) ==========

    /**
     * GET /detalleventa
     * Lista todos los detalles de venta en formato JSON.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetalleVenta>> listarRest() {
        return ResponseEntity.ok(detalleVentaService.listarTodos());
    }

    /**
     * GET /detalleventa/{codigo}
     * Busca un detalle de venta por su código (ID).
     * Retorna 404 si no existe.
     */
    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable Long codigo) {
        return detalleVentaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /detalleventa/venta/{codigoVenta}
     * Obtiene todos los detalles asociados a una venta específica.
     * Retorna 404 si no hay detalles para esa venta.
     */
    @GetMapping("/venta/{codigoVenta}")
    @ResponseBody
    public ResponseEntity<List<DetalleVenta>> buscarPorVenta(@PathVariable Long codigoVenta) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorVenta(codigoVenta);
        return detalles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(detalles);
    }

    /**
     * GET /detalleventa/producto/{codigoProducto}
     * Obtiene todos los detalles que corresponden a un producto concreto.
     * Retorna 404 si no hay ninguno.
     */
    @GetMapping("/producto/{codigoProducto}")
    @ResponseBody
    public ResponseEntity<List<DetalleVenta>> buscarPorProducto(@PathVariable Long codigoProducto) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorProducto(codigoProducto);
        return detalles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(detalles);
    }

    /**
     * GET /detalleventa/estado/{estado}
     * Filtra detalles por su estado (ej: activo, cancelado, etc.).
     * El estado es un número entero que representa un enum en la lógica de negocio.
     */
    @GetMapping("/estado/{estado}")
    @ResponseBody
    public ResponseEntity<List<DetalleVenta>> buscarPorEstado(@PathVariable int estado) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorEstado(estado);
        return detalles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(detalles);
    }

    /**
     * POST /detalleventa
     * Crea un nuevo detalle de venta a partir de JSON.
     * Retorna 201 Created si todo va bien, o 400 Bad Request si hay error de validación.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> guardarRest(@RequestBody DetalleVenta detalleVenta) {
        try {
            return new ResponseEntity<>(detalleVentaService.guardar(detalleVenta), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * DELETE /detalleventa/{codigo}
     * Elimina un detalle de venta por su código.
     * Retorna 204 No Content si se elimina correctamente, 404 si no existe.
     * Nota: En la parte web este método no se usa porque se bloquea la eliminación por razones contables.
     */
    @DeleteMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Void> eliminarRest(@PathVariable Long codigo) {
        if (!detalleVentaService.existePorCodigo(codigo)) return ResponseEntity.notFound().build();
        detalleVentaService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /detalleventa/{codigo}
     * Actualiza completamente un detalle de venta existente.
     * Retorna 200 OK con el objeto actualizado, 404 si no se encuentra, o 400 si hay error.
     */
    @PutMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<?> actualizarRest(@PathVariable Long codigo, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) return ResponseEntity.notFound().build();
            detalleVenta.setCodigoDetalleVenta(codigo);
            return ResponseEntity.ok(detalleVentaService.actualizar(codigo, detalleVenta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ========== ENDPOINTS PARA VISTAS WEB ==========

    /**
     * GET /detalleventa/web
     * Muestra la lista de todos los detalles de venta en una página HTML.
     * Los datos se inyectan en el modelo con nombre "detalles".
     */
    @GetMapping("/web")
    public String listarWeb(Model model) {
        model.addAttribute("detalles", detalleVentaService.listarTodos());
        return "detalleventa/detalleventa";
    }

    /**
     * GET /detalleventa/web/nuevo
     * Despliega el formulario para crear un nuevo detalle de venta.
     * Se pasan al modelo: un objeto DetalleVenta vacío, la lista de productos y la lista de ventas.
     */
    @GetMapping("/web/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("detalle", new DetalleVenta());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("ventas", ventasService.listarTodos());
        return "detalleventa/detalleventa";
    }

    /**
     * POST /detalleventa/web/guardar
     * Procesa el envío del formulario para crear un nuevo detalle de venta.
     * Usa RedirectAttributes para mostrar mensajes flash de éxito o error.
     */
    @PostMapping("/web/guardar")
    public String guardarWeb(@ModelAttribute DetalleVenta detalle, RedirectAttributes redirectAttributes) {
        try {
            detalleVentaService.guardar(detalle);
            redirectAttributes.addFlashAttribute("success", "Detalle guardado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/detalleventa/web";
    }

    /**
     * GET /detalleventa/web/editar/{codigo}
     * Muestra el formulario de edición precargado con los datos del detalle existente.
     * También carga las listas de productos y ventas para los desplegables.
     * Si no existe el detalle, redirige con mensaje de error.
     */
    @GetMapping("/web/editar/{codigo}")
    public String editarFormulario(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes) {
        try {
            DetalleVenta detalle = detalleVentaService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));
            model.addAttribute("detalle", detalle);
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("ventas", ventasService.listarTodos());
            return "detalleventa/detalleventa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/detalleventa/web";
        }
    }

    /**
     * POST /detalleventa/web/actualizar/{codigo}
     * Procesa la actualización de un detalle de venta desde el formulario.
     * El código se pasa por la URL y el objeto con los nuevos datos viene del formulario.
     */
    @PostMapping("/web/actualizar/{codigo}")
    public String actualizarWeb(@PathVariable Long codigo, @ModelAttribute DetalleVenta detalle, RedirectAttributes redirectAttributes) {
        try {
            detalleVentaService.actualizar(codigo, detalle);
            redirectAttributes.addFlashAttribute("success", "Detalle actualizado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/detalleventa/web";
    }

    /**
     * GET /detalleventa/web/eliminar/{codigo}
     * Intento de eliminación desde la interfaz web.
     * Está deshabilitado porque eliminar detalles de venta afectaría la integridad contable.
     * Se muestra un mensaje de error indicando que la operación constituye fraude.
     */
    @GetMapping("/web/eliminar/{codigo}")
    public String eliminarWeb(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "❌ No se permite eliminar detalles de venta. Eliminar registros contables constituye fraude.");
        return "redirect:/detalleventa/web";
    }
}