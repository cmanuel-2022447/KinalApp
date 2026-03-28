package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    // Interface: Es un contrato que dice QUÉ métodos debe tener
    // cualquier servicio de Usuario, no tiene
    // implementación, solo la definición de los métodos

    /*
     * readOnly = true: Lo que hace es optimizar la consulta no bloquea la BD
     */
    @Transactional(readOnly = true)
    List<Usuario> listarTodos();
    // List<Usuario> lo que hace es devolver una lista de objetos de la entidad Usuario

    // Metodo que guarda un Usuario en la BD
    Usuario guardar(Usuario usuario);

    // Optional - Contenedor que puede o no tener un valor evita el error de NullPointerException
    Optional<Usuario> buscarPorCodigo(Long codigo);

    @Transactional(readOnly = true)
    List<Usuario> buscarPorEstado(int estado);

    // Metodo que actualiza un Usuario
    Usuario actualizar(Long codigo, Usuario usuario);

    // Metodo de tipo void para eliminar un Usuario (no retorna ningún dato)
    void eliminar(Long codigo);

    // boolean - Retorna true si existe, false si no existe
    boolean existePorCodigo(Long codigo);
}