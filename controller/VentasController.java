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
    // El controlador solo debe de tener conexion con el servicio
    private final IVentasService ventasService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public VentasController(IVentasService ventasService) {
        this.ventasService = ventasService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Ventas>> listar() {
        // delegamos al servicio y retornamos 200 ok con la lista de Ventas
        return ResponseEntity.ok(ventasService.listarTodos());
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Ventas> buscarPorCodigo(@PathVariable Long codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return ventasService.buscarPorCodigo(codigo)
                // si optional tiene valor, devuelve 200 ok con la venta
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Ventas>> buscarPorEstado(@PathVariable int estado) {
        List<Ventas> ventas = ventasService.buscarPorEstado(estado);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return ventas.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ventas);
    }

    // POST crear una nueva venta
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Ventas ventas) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Ventas
        // <?> significa "tipo generico" puede ser un Ventas o un String
        try {
            return new ResponseEntity<>(ventasService.guardar(ventas), HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de una venta)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar una venta
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        if (!ventasService.existePorCodigo(codigo)) {
            // Si no existe devuelve 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
        ventasService.eliminar(codigo);
        return ResponseEntity.noContent().build();
        // 204 NO CONTENT
    }

    // Actualizar venta a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigo, @RequestBody Ventas ventas) {
        try {
            if (!ventasService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar la venta y retornar 200 ok con la venta ya actualizada
            return ResponseEntity.ok(ventasService.actualizar(codigo, ventas));
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }
}