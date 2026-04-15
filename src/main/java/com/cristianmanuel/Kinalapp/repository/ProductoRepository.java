package com.cristianmanuel.Kinalapp.repository;

import com.cristianmanuel.Kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio para la entidad Producto.
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Long = codigoProducto
}