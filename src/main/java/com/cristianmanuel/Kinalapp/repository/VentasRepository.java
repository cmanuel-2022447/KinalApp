package com.cristianmanuel.Kinalapp.repository;

import com.cristianmanuel.Kinalapp.entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio para la entidad Ventas.
public interface VentasRepository extends JpaRepository<Ventas, Long> {
    // Long = codigoVenta
}