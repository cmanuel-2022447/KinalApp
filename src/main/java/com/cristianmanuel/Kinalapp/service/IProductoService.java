package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Producto;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IProductoService {

    @Transactional(readOnly = true)
    List<Producto> listarTodos();

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorCodigo(Long codigo);

    @Transactional(readOnly = true)
    List<Producto> buscarPorEstado(int estado);

    Producto actualizar(Long codigo, Producto producto);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);
}