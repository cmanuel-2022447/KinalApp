package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@RestController = @Controller + @ResponseBody
@RequestMapping("/clientes")
// Todas las rutas en este controlador deben empezar con /clientes
public class ClienteController {

    // Inyectamos el SERVICIO y NO el repositorio
    // El controlador solo debe de tener conexion con el servidor
    private final IClienteService clienteService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Responde a peticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Cliente>> listar() {
        List<Cliente> clientes = clienteService.listarTodos();
        // delegamos al servicio y retornamos 200 ok
        return ResponseEntity.ok(clientes);
        //200 ok con la lista de Cliente
    }

    /*
     * {dpi} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable Integer dpi) {
        //@PathVariable Toma el valor de la URL y lo asigna al dpi
        return clienteService.buscarPorDPI(dpi)
                //si optional tiene el valor de la URL y lo asigna al dpi
                .map(ResponseEntity::ok)
                // Si Optional esta vacio,devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cliente>> buscarPorEstado(@PathVariable int estado) {
        List<Cliente> clientes = clienteService.buscarPorEstado(estado);
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientes);
    }

    // POST crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente) {
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Cliente
        // <?> significa "tipo generico" puede ser un Cliente o un String
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Intentamos guardar el cliente pero puede lazar una excepcion
            //de IlegalArgumentsException
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            // 201 CREATED(mucho mas especifico que el 200 para la creacion de un cliente)
        } catch (IllegalArgumentException e) {
            //si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer dpi) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return ResponseEntity.notFound().build();
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar cliente a través de DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable Integer dpi, @RequestBody Cliente cliente) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                //Verificar si existe antes de poder actualizar
                //404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero esto puede lanzar una excepcion
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
            //200 ok con el cliente ya actualizado
        } catch (IllegalArgumentException e) {
            //Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            //Posiblemente cualquier otro error como: Cliente no encontrado, etc.
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}