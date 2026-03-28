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
    // El controlador solo debe de tener conexion con el servicio
    private final IUsuarioService usuarioService;

    // Como buena práctica la Inyección de dependencias debe hacerse por el constructor
    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Responde a peticiones GET
    @GetMapping
    // ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Usuario>> listar() {
        // delegamos al servicio y retornamos 200 ok con la lista de Usuario
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /*
     * {codigo} es una variable de ruta (valor a buscar)
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable Long codigo) {
        // @PathVariable Toma el valor de la URL y lo asigna al codigo
        return usuarioService.buscarPorCodigo(codigo)
                // si optional tiene valor, devuelve 200 ok con el usuario
                .map(ResponseEntity::ok)
                // Si Optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> buscarPorEstado(@PathVariable int estado) {
        List<Usuario> usuarios = usuarioService.buscarPorEstado(estado);
        // Si la lista esta vacia devuelve 404 NOT FOUND, de lo contrario 200 ok
        return usuarios.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(usuarios);
    }

    // POST crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        // @RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Usuario
        // <?> significa "tipo generico" puede ser un Usuario o un String
        try {
            return new ResponseEntity<>(usuarioService.guardar(usuario), HttpStatus.CREATED);
            // 201 CREATED (mucho mas especifico que el 200 para la creacion de un usuario)
        } catch (IllegalArgumentException e) {
            // si hay error de validaciones
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }

    // DELETE eliminar un usuario
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigo) {
        // ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        if (!usuarioService.existePorCodigo(codigo)) {
            // Si no existe devuelve 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(codigo);
        return ResponseEntity.noContent().build();
        // 204 NO CONTENT
    }

    // Actualizar usuario a través de su código
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigo, @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                // Verificar si existe antes de poder actualizar
                // 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            // Actualizar el usuario y retornar 200 ok con el usuario ya actualizado
            return ResponseEntity.ok(usuarioService.actualizar(codigo, usuario));
        } catch (IllegalArgumentException e) {
            // Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST con mensaje de error
        }
    }
}