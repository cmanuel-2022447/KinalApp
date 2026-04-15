package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.DetalleVenta;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    @Transactional(readOnly = true)
    List<DetalleVenta> listarTodos();

    DetalleVenta guardar(DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPorCodigo(Long codigo);

    @Transactional(readOnly = true)
    List<DetalleVenta> buscarPorEstado(int estado);

    DetalleVenta actualizar(Long codigo, DetalleVenta detalleVenta);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);

    // Métodos de consulta específicos
    List<DetalleVenta> buscarPorVenta(Long codigoVenta);      // Todos los detalles de una venta
    List<DetalleVenta> buscarPorProducto(Long codigoProducto); // Todos los detalles que incluyen un producto
}