package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RestController = @Controller + @ResponseBody
@RequestMapping("/productos")
// Todas las rutas en este controlador deben empezar con /productos
public class ProductoController {

    // Inyectamos el SERVICIO y NO el repositorio
    // El controlador solo debe de tener conexion con el servicio
    private final IProductoService productoService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Producto>> listar() {
        // delegamos al servicio y retornamos 200 ok con la lista de Producto
        return ResponseEntity.ok(productoService.listarTodos());
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable Long codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return productoService.buscarPorCodigo(codigo)
                // si optional tiene valor, devuelve 200 ok con el producto
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> buscarPorEstado(@PathVariable int estado) {
        List<Producto> productos = productoService.buscarPorEstado(estado);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return productos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(productos);
    }

    // POST crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Producto
        // <?> significa "tipo generico" puede ser un Producto o un String
        try {
            return new ResponseEntity<>(productoService.guardar(producto), HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un producto)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un producto
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        if (!productoService.existePorCodigo(codigo)) {
            // Si no existe devuelve 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(codigo);
        return ResponseEntity.noContent().build();
        // 204 NO CONTENT
    }

    // Actualizar producto a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigo, @RequestBody Producto producto) {
        try {
            if (!productoService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar el producto y retornar 200 ok con el producto ya actualizado
            return ResponseEntity.ok(productoService.actualizar(codigo, producto));
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }
}