package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface IClienteService {

    // readOnly = true: solo lectura, no bloquea la base de datos para escritura, mejora rendimiento
    @Transactional(readOnly = true)
    List<Cliente> listarTodos();  // Recupera todos los clientes de la base de datos

    // Guarda un nuevo cliente o actualiza uno existente si ya tiene ID
    Cliente guardar(Cliente cliente);

    // Optional evita NullPointerException: puede contener un Cliente o estar vacío
    Optional<Cliente> buscarPorDPI(Long dpi);  // Busca por clave primaria (DPI)

    // Filtra clientes por estado (1 = activo, 0 = inactivo)
    @Transactional(readOnly = true)
    List<Cliente> buscarPorEstado(int estado);

    // Actualiza los datos de un cliente existente identificado por su DPI
    Cliente actualizar(Long dpi, Cliente cliente);

    // Elimina un cliente por su DPI (void = no retorna datos)
    void eliminar(Long dpi);

    // Verifica existencia de un cliente por DPI (útil antes de operaciones)
    boolean existePorDPI(Long dpi);
}