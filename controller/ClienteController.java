package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import com.cristianmanuel.Kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RestController = @Controller + @ResponseBody
@RequestMapping("/clientes")
// Todas las rutas en este controlador deben empezar con /clientes
public class ClienteController {

    // Inyectamos el SERVICIO y NO el repositorio
    // El controlador solo debe de tener conexion con el servicio
    private final IClienteService clienteService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Cliente>> listar() {
        // delegamos al servicio y retornamos 200 ok con la lista de Cliente
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    /*
     * {dpi} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable Long dpi) {
        // @PathVariable Toma el valor de la URL y lo asigna al dpi
        return clienteService.buscarPorDPI(dpi)
                // si optional tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cliente>> buscarPorEstado(@PathVariable int estado) {
        List<Cliente> clientes = clienteService.buscarPorEstado(estado);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return clientes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(clientes);
    }

    // POST crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Cliente
        // <?> significa "tipo generico" puede ser un Cliente o un String
        try {
            return new ResponseEntity<>(clienteService.guardar(cliente), HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un cliente)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable Long dpi) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        if (!clienteService.existePorDPI(dpi)) {
            // Si no existe devuelve 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
        clienteService.eliminar(dpi);
        return ResponseEntity.noContent().build();
        // 204 NO CONTENT
    }

    // Actualizar cliente a través de su DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable Long dpi, @RequestBody Cliente cliente) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar el cliente y retornar 200 ok con el cliente ya actualizado
            return ResponseEntity.ok(clienteService.actualizar(dpi, cliente));
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }
}