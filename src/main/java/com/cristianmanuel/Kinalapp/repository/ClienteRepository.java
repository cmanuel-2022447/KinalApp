package com.cristianmanuel.Kinalapp.repository;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio JPA para la entidad Cliente. Proporciona métodos CRUD básicos.
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Long es el tipo de la clave primaria (dpi_cliente)
}