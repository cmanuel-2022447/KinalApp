package com.cristianmanuel.Kinalapp.repository;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marca explícitamente como bean de repositorio (opcional, JpaRepository ya lo trae)
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuarios por username. Retorna List para evitar excepción si hay duplicados.
    List<Usuario> findByUsername(String username);

    // Verifica si ya existe un username (para validar registro)
    boolean existsByUsername(String username);

    // Verifica si ya existe un email (para evitar duplicados)
    boolean existsByEmail(String email);
}