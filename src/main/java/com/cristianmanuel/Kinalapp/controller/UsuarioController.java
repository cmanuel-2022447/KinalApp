package com.cristianmanuel.Kinalapp.controller;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// @RestController = @Controller + @ResponseBody
@RequestMapping("/usuarios")
// Todas las rutas en este controlador deben empezar con /usuarios
public class UsuarioController {

    // Inyectamos el SERVICIO y NO el repositorio
    // El controlador solo debe de tener conexion con el servidor
    private final IUsuarioService usuarioService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        // delegamos al servicio y retornamos 200 ok
        return ResponseEntity.ok(usuarios);
        // 200 ok con la lista de Usuario
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable Integer codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return usuarioService.buscarPorCodigo(codigo)
                // si optional tiene el valor de la URL y lo asigna al codigo
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> buscarPorEstado(@PathVariable int estado) {
        List<Usuario> usuarios = usuarioService.buscarPorEstado(estado);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    // POST crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Usuario
        // <?> significa "tipo generico" puede ser un Usuario o un String
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            // Intentamos guardar el usuario pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un usuario)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un usuario
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            usuarioService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar usuario a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Integer codigo, @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar el usuario pero esto puede lanzar una excepcion
            Usuario usuarioActualizado = usuarioService.actualizar(codigo, usuario);
            return ResponseEntity.ok(usuarioActualizado);
            // 200 ok con el usuario ya actualizado
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Posiblemente cualquier otro error como: Usuario no encontrado, etc.
            // 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}