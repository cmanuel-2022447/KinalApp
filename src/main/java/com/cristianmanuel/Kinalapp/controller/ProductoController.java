package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador CRUD para la entidad Producto.
 * Expone dos interfaces:
 * - REST API (JSON) para consumir desde frontend externo o aplicaciones móviles.
 * - Vistas web (Thymeleaf) para manejo desde navegador con formularios HTML.
 */
@Controller  // Indica que es un controlador Spring.
@RequestMapping("/productos")  // Ruta base para todos los endpoints de este controlador.
public class ProductoController {

    // Servicio que contiene la lógica de negocio para productos.
    private final IProductoService productoService;

    // Constructor: inyección de dependencia del servicio.
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // ========== ENDPOINTS REST (devuelven JSON) ==========

    /**
     * Lista todos los productos en formato JSON.
     * GET /productos
     * @return ResponseEntity con la lista y código HTTP 200 OK.
     */
    @GetMapping  // Sin path adicional, responde a GET /productos
    @ResponseBody  // El valor retornado se escribe directamente en el cuerpo de la respuesta (no es vista).
    public ResponseEntity<List<Producto>> listarRest() {
        // productoService.listarTodos() devuelve todos los productos.
        // ResponseEntity.ok() construye una respuesta con código 200 y el cuerpo.
        return ResponseEntity.ok(productoService.listarTodos());
    }

    /**
     * Crea un nuevo producto a partir de JSON enviado en el cuerpo de la petición.
     * POST /productos
     * @param producto objeto Producto deserializado automáticamente desde el JSON
     * @return 201 Created con el producto guardado, o 400 Bad Request si hay error de validación.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> guardarRest(@RequestBody Producto producto) {
        try {
            // guardar() puede lanzar IllegalArgumentException (ej: nombre duplicado)
            Producto guardado = productoService.guardar(producto);
            // HttpStatus.CREATED = 201
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Retornamos el mensaje de error con código 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza un producto existente.
     * PUT /productos/{codigo}
     * @param codigo identificador del producto (viene en la URL)
     * @param producto datos actualizados en JSON
     * @return 200 OK con producto actualizado, 404 si no existe, 400 si error.
     */
    @PutMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<?> actualizarRest(@PathVariable Long codigo, @RequestBody Producto producto) {
        try {
            // Verificamos si el producto existe antes de actualizar.
            if (!productoService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();  // 404 Not Found
            }
            // actualizar() retorna el producto actualizado.
            return ResponseEntity.ok(productoService.actualizar(codigo, producto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un producto.
     * DELETE /productos/{codigo}
     * @param codigo identificador del producto a eliminar
     * @return 204 No Content si éxito, 404 si no existe, 400 si error (ej: integridad referencial).
     */
    @DeleteMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<?> eliminarRest(@PathVariable Long codigo) {
        // Verificamos existencia.
        if (!productoService.existePorCodigo(codigo)) {
            return ResponseEntity.notFound().build();
        }
        try {
            productoService.eliminar(codigo);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ========== ENDPOINTS PARA VISTAS WEB (HTML) ==========

    /**
     * Muestra la lista de productos en una página HTML.
     * GET /productos/web
     * @param model objeto Model de Spring para pasar atributos a la vista.
     * @return nombre de la plantilla "productos/list"
     */
    @GetMapping("/web")
    public String listarWeb(Model model) {
        // Agrega al modelo la lista de productos bajo el nombre "productos"
        model.addAttribute("productos", productoService.listarTodos());
        return "productos/productos";  // Se espera list.html en src/main/resources/templates/productos/
    }

    /**
     * Muestra el formulario para crear un nuevo producto.
     * GET /productos/web/nuevo
     * @param model se añade un objeto Producto vacío para que el formulario lo enlace.
     * @return "productos/form"
     */
    @GetMapping("/web/nuevo")
    public String nuevoFormulario(Model model) {
        // Se crea una instancia vacía para que Thymeleaf pueda usar sus propiedades en el formulario.
        model.addAttribute("producto", new Producto());
        return "productos/productos";  // form.html en templates/productos/
    }

    /**
     * Procesa el formulario de creación de producto.
     * POST /productos/web/guardar
     * @param producto objeto bindeado desde los campos del formulario (gracias a @ModelAttribute)
     * @param redirectAttributes para mensajes flash de éxito/error
     * @return redirección a la lista de productos
     */
    @PostMapping("/web/guardar")
    public String guardarWeb(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.guardar(producto);
            // addFlashAttribute: el mensaje estará disponible solo en la siguiente petición (redirección)
            redirectAttributes.addFlashAttribute("success", "Producto guardado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos/web";  // Redirige a la lista después de guardar.
    }

    /**
     * Muestra el formulario de edición precargado con los datos del producto existente.
     * GET /productos/web/editar/{codigo}
     * @param codigo ID del producto a editar
     * @param model modelo donde se pone el producto encontrado
     * @param redirectAttributes para errores si no se encuentra
     * @return vista del formulario o redirección si hay error
     */
    @GetMapping("/web/editar/{codigo}")
    public String editarFormulario(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Buscar el producto; si no existe, lanza excepción.
            Producto producto = productoService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            model.addAttribute("producto", producto);
            return "productos/productos";  // Reutilizamos la misma plantilla form.html
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos/web";
        }
    }

    /**
     * Procesa la actualización de un producto desde el formulario web.
     * POST /productos/web/actualizar/{codigo}
     * @param codigo ID del producto a actualizar (viene en URL)
     * @param producto objeto con los nuevos datos (bind desde formulario)
     * @param redirectAttributes para mensajes flash
     * @return redirección a la lista
     */
    @PostMapping("/web/actualizar/{codigo}")
    public String actualizarWeb(@PathVariable Long codigo, @ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizar(codigo, producto);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos/web";
    }

    /**
     * Elimina un producto desde la interfaz web.
     * GET /productos/web/eliminar/{codigo}
     * Nota: Se usa GET para simplificar (enlace directo), aunque lo correcto sería DELETE o POST.
     * @param codigo ID del producto
     * @param redirectAttributes mensajes flash
     * @return redirección a la lista
     */
    @GetMapping("/web/eliminar/{codigo}")
    public String eliminarWeb(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos/web";
    }
}