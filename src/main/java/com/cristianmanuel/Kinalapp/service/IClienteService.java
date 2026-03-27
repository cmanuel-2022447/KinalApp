package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IClienteService {
    // Interfaz: Es un contrato que dice QUÉ métodos debe tener
    // cualquier servicio de Clientes, No tiene
    // implementación, solo la definición de los métodos

    /*
     * readOnly = true: Lo que hace es optimizar la consulta no bloquea la BD
     */
    @Transactional(readOnly = true)
    List<Cliente> listarTodos();
    // list<Cliente> lo que hace es devolver una lista de objetos de la entidad Cliente

    // Metodo que guarda un Cliente en la BD
    Cliente guardar(Cliente cliente);

    // Optional - Contenedor que puede o no tener un valor evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(Integer dpi);

    @Transactional(readOnly = true)
    List<Cliente> buscarPorEstado(int estado);

    // Metodo que actualiza un Cliente
    Cliente actualizar(Integer dpi, Cliente cliente);

    // Metodo de tipo void para eliminar a un Cliente (no retorna ningún dato)
    void eliminar(Integer dpi);

    // boolean - Retorna true si existe, false si no existe
    boolean existePorDPI(Integer dpi);
}