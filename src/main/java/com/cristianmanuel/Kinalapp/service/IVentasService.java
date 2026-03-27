package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Ventas;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IVentasService {
    // Interfaz: Es un contrato que dice QUÉ métodos debe tener
    // cualquier servicio de Ventas, No tiene
    // implementación, solo la definición de los métodos

    /*
     * readOnly = true: Lo que hace es optimizar la consulta no bloquea la BD
     */
    @Transactional(readOnly = true)
    List<Ventas> listarTodos();
    // list<Ventas> lo que hace es devolver una lista de objetos de la entidad Ventas

    // Metodo que guarda una Venta en la BD
    Ventas guardar(Ventas ventas);

    // Optional - Contenedor que puede o no tener un valor evita el error de NullPointerException
    Optional<Ventas> buscarPorCodigo(Integer codigo);

    @Transactional(readOnly = true)
    List<Ventas> buscarPorEstado(int estado);

    // Metodo que actualiza una Venta
    Ventas actualizar(Integer codigo, Ventas ventas);

    // Metodo de tipo void para eliminar una Venta (no retorna ningún dato)
    void eliminar(Integer codigo);

    // boolean - Retorna true si existe, false si no existe
    boolean existePorCodigo(Integer codigo);
}