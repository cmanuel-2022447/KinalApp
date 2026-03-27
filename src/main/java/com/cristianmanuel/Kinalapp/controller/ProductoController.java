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
    // El controlador solo debe de tener conexion con el servidor
    private final IProductoService productoService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.listarTodos();
        // delegamos al servicio y retornamos 200 ok
        return ResponseEntity.ok(productos);
        // 200 ok con la lista de Producto
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable Integer codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return productoService.buscarPorCodigo(codigo)
                // si optional tiene el valor de la URL y lo asigna al codigo
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> buscarPorEstado(@PathVariable int estado) {
        List<Producto> productos = productoService.buscarPorEstado(estado);
        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productos);
    }

    // POST crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Producto
        // <?> significa "tipo generico" puede ser un Producto o un String
        try {
            Producto nuevoProducto = productoService.guardar(producto);
            // Intentamos guardar el producto pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un producto)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un producto
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!productoService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            productoService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar producto a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody Producto producto) {
        try {
            if (!productoService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar el producto pero esto puede lanzar una excepcion
            Producto productoActualizado = productoService.actualizar(codigo, producto);
            return ResponseEntity.ok(productoActualizado);
            // 200 ok con el producto ya actualizado
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Posiblemente cualquier otro error como: Producto no encontrado, etc.
            // 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}