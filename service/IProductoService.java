package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Producto;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
    // Interface: Es un contrato que dice QUÉ métodos debe tener
    // cualquier servicio de Producto, no tiene
    // implementación, solo la definición de los métodos

    /*
     * readOnly = true: Lo que hace es optimizar la consulta no bloquea la BD
     */
    @Transactional(readOnly = true)
    List<Producto> listarTodos();
    // List<Producto> lo que hace es devolver una lista de objetos de la entidad Producto

    // Metodo que guarda un Producto en la BD
    Producto guardar(Producto producto);

    // Optional - Contenedor que puede o no tener un valor evita el error de NullPointerException
    Optional<Producto> buscarPorCodigo(Long codigo);

    @Transactional(readOnly = true)
    List<Producto> buscarPorEstado(int estado);

    // Metodo que actualiza un Producto
    Producto actualizar(Long codigo, Producto producto);

    // Metodo de tipo void para eliminar un Producto (no retorna ningún dato)
    void eliminar(Long codigo);

    // boolean - Retorna true si existe, false si no existe
    boolean existePorCodigo(Long codigo);
}