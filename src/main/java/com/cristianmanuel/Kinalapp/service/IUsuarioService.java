package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    @Transactional(readOnly = true)
    List<Usuario> listarTodos();

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCodigo(Long codigo);

    @Transactional(readOnly = true)
    List<Usuario> buscarPorEstado(int estado);

    Usuario actualizar(Long codigo, Usuario usuario);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);

    // Autenticación: busca por username y verifica password
    @Transactional(readOnly = true)
    Optional<Usuario> login(String username, String password);

    // Validación de unicidad para registro
    @Transactional(readOnly = true)
    boolean existeUsername(String username);

    @Transactional(readOnly = true)
    boolean existeEmail(String email);
}