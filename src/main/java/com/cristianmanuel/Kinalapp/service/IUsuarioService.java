package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    // Interfaz: Es un contrato que dice QUÉ métodos debe tener
    // cualquier servicio de Usuarios, No tiene
    // implementación, solo la definición de los métodos

    /*
     * readOnly = true: Lo que hace es optimizar la consulta no bloquea la BD
     */
    @Transactional(readOnly = true)
    List<Usuario> listarTodos();
    // list<Usuario> lo que hace es devolver una lista de objetos de la entidad Usuario

    // Metodo que guarda un Usuario en la BD
    Usuario guardar(Usuario usuario);

    // Optional - Contenedor que puede o no tener un valor evita el error de NullPointerException
    Optional<Usuario> buscarPorCodigo(Integer codigo);

    @Transactional(readOnly = true)
    List<Usuario> buscarPorEstado(int estado);

    // Metodo que actualiza un Usuario
    Usuario actualizar(Integer codigo, Usuario usuario);

    // Metodo de tipo void para eliminar un Usuario (no retorna ningún dato)
    void eliminar(Integer codigo);

    // boolean - Retorna true si existe, false si no existe
    boolean existePorCodigo(Integer codigo);
}