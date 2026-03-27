package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.DetalleVenta;
import com.cristianmanuel.Kinalapp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// @RestController = @Controller + @ResponseBody
@RequestMapping("/detalleventa")
// Todas las rutas en este controlador deben empezar con /detalleventa
public class DetalleVentaController {

    // Inyectamos el SERVICIO y NO el repositorio
    // El controlador solo debe de tener conexion con el servidor
    private final IDetalleVentaService detalleVentaService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<DetalleVenta>> listar() {
        List<DetalleVenta> detalles = detalleVentaService.listarTodos();
        // delegamos al servicio y retornamos 200 ok
        return ResponseEntity.ok(detalles);
        // 200 ok con la lista de DetalleVenta
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable Integer codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return detalleVentaService.buscarPorCodigo(codigo)
                // si optional tiene el valor de la URL y lo asigna al codigo
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * {codigoVenta} es una variable de ruta para buscar por venta
     */
    @GetMapping("/venta/{codigoVenta}")
    public ResponseEntity<List<DetalleVenta>> buscarPorVenta(@PathVariable Integer codigoVenta) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorVenta(codigoVenta);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    /*
     * {codigoProducto} es una variable de ruta para buscar por producto
     */
    @GetMapping("/producto/{codigoProducto}")
    public ResponseEntity<List<DetalleVenta>> buscarPorProducto(@PathVariable Integer codigoProducto) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorProducto(codigoProducto);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DetalleVenta>> buscarPorEstado(@PathVariable int estado) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorEstado(estado);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    // POST crear un nuevo detalle de venta
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo DetalleVenta
        // <?> significa "tipo generico" puede ser un DetalleVenta o un String
        try {
            DetalleVenta nuevoDetalle = detalleVentaService.guardar(detalleVenta);
            // Intentamos guardar el detalle pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un detalle)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un detalle de venta
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar detalle de venta a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar el detalle pero esto puede lanzar una excepcion
            detalleVenta.setCodigoDetalleVenta(codigo);
            DetalleVenta detalleActualizado = detalleVentaService.actualizar(codigo, detalleVenta);
            return ResponseEntity.ok(detalleActualizado);
            // 200 ok con el detalle ya actualizado
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Posiblemente cualquier otro error como: Detalle no encontrado, etc.
            // 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}