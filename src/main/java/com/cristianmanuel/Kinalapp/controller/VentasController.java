package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Ventas;
import com.cristianmanuel.Kinalapp.service.IVentasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// @RestController = @Controller + @ResponseBody
@RequestMapping("/ventas")
// Todas las rutas en este controlador deben empezar con /ventas
public class VentasController {

    // Inyectamos el SERVICIO y NO el repositorio
    // El controlador solo debe de tener conexion con el servidor
    private final IVentasService ventasService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public VentasController(IVentasService ventasService) {
        this.ventasService = ventasService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Ventas>> listar() {
        List<Ventas> ventas = ventasService.listarTodos();
        // delegamos al servicio y retornamos 200 ok
        return ResponseEntity.ok(ventas);
        // 200 ok con la lista de Ventas
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Ventas> buscarPorCodigo(@PathVariable Integer codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return ventasService.buscarPorCodigo(codigo)
                // si optional tiene el valor de la URL y lo asigna al codigo
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Ventas>> buscarPorEstado(@PathVariable int estado) {
        List<Ventas> ventas = ventasService.buscarPorEstado(estado);
        if (ventas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ventas);
    }

    // POST crear una nueva venta
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Ventas ventas) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Ventas
        // <?> significa "tipo generico" puede ser un Ventas o un String
        try {
            Ventas nuevaVenta = ventasService.guardar(ventas);
            // Intentamos guardar la venta pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de una venta)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar una venta
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!ventasService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            ventasService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar venta a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody Ventas ventas) {
        try {
            if (!ventasService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar la venta pero esto puede lanzar una excepcion
            Ventas ventaActualizada = ventasService.actualizar(codigo, ventas);
            return ResponseEntity.ok(ventaActualizada);
            // 200 ok con la venta ya actualizada
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Posiblemente cualquier otro error como: Venta no encontrada, etc.
            // 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}