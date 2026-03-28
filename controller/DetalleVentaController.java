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
    // El controlador solo debe de tener conexion con el servicio
    private final IDetalleVentaService detalleVentaService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<DetalleVenta>> listar() {
        // delegamos al servicio y retornamos 200 ok con la lista de DetalleVenta
        return ResponseEntity.ok(detalleVentaService.listarTodos());
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable Long codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return detalleVentaService.buscarPorCodigo(codigo)
                // si optional tiene valor, devuelve 200 ok con el detalle
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * {codigoVenta} es una variable de ruta para buscar por venta
     */
    @GetMapping("/venta/{codigoVenta}")
    public ResponseEntity<List<DetalleVenta>> buscarPorVenta(@PathVariable Long codigoVenta) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorVenta(codigoVenta);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return detalles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(detalles);
    }

    /*
     * {codigoProducto} es una variable de ruta para buscar por producto
     */
    @GetMapping("/producto/{codigoProducto}")
    public ResponseEntity<List<DetalleVenta>> buscarPorProducto(@PathVariable Long codigoProducto) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorProducto(codigoProducto);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return detalles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(detalles);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DetalleVenta>> buscarPorEstado(@PathVariable int estado) {
        List<DetalleVenta> detalles = detalleVentaService.buscarPorEstado(estado);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return detalles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(detalles);
    }

    // POST crear un nuevo detalle de venta
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo DetalleVenta
        // <?> significa "tipo generico" puede ser un DetalleVenta o un String
        try {
            return new ResponseEntity<>(detalleVentaService.guardar(detalleVenta), HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un detalle)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un detalle de venta
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        if (!detalleVentaService.existePorCodigo(codigo)) {
            // Si no existe devuelve 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
        detalleVentaService.eliminar(codigo);
        return ResponseEntity.noContent().build();
        // 204 NO CONTENT
    }

    // Actualizar detalle de venta a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigo, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // por seguridad usamos el código de la URL y no el que viene en el JSON
            detalleVenta.setCodigoDetalleVenta(codigo);
            // Actualizar el detalle y retornar 200 ok con el detalle ya actualizado
            return ResponseEntity.ok(detalleVentaService.actualizar(codigo, detalleVenta));
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }
}