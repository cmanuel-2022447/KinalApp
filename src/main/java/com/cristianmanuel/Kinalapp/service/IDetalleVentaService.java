package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.DetalleVenta;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    // Interfaz: Es un contrato que dice QUÉ métodos debe tener
    // cualquier servicio de DetalleVenta, No tiene
    // implementación, solo la definición de los métodos

    /*
     * readOnly = true: Lo que hace es optimizar la consulta no bloquea la BD
     */
    @Transactional(readOnly = true)
    List<DetalleVenta> listarTodos();
    // list<DetalleVenta> lo que hace es devolver una lista de objetos de la entidad DetalleVenta

    // Metodo que guarda un DetalleVenta en la BD
    DetalleVenta guardar(DetalleVenta detalleVenta);

    // Optional - Contenedor que puede o no tener un valor evita el error de NullPointerException
    Optional<DetalleVenta> buscarPorCodigo(Integer codigo);

    @Transactional(readOnly = true)
    List<DetalleVenta> buscarPorEstado(int estado);

    // Metodo que actualiza un DetalleVenta
    DetalleVenta actualizar(Integer codigo, DetalleVenta detalleVenta);

    // Metodo de tipo void para eliminar un DetalleVenta (no retorna ningún dato)
    void eliminar(Integer codigo);

    // boolean - Retorna true si existe, false si no existe
    boolean existePorCodigo(Integer codigo);

    // Metodo para buscar detalles por venta
    List<DetalleVenta> buscarPorVenta(Integer codigoVenta);

    // Metodo para buscar detalles por producto
    List<DetalleVenta> buscarPorProducto(Integer codigoProducto);
}