package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import com.cristianmanuel.Kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Anotación que registra un bean
 * Que la clase contiene la lógica del negocio
 */
@Service
/*
 * Por defecto, todos los metodos de esta clase seran transaccionales
 * Una Transaccion es que puede o no ocurrir algo
 */
@Transactional
public class ClienteService implements IClienteService {

    /*
     * private: solo es accesible dentro de la misma clase
     * final: no puede cambiar, es constante
     * ClienteRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya que Spring nos da el repositorio
     */
    private final ClienteRepository clienteRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parametros: Spring pasa el repositorio automaticamente y a esto se le conoce
     * como Inyección de Dependencias
     */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /*
     * @Override: Indica que estamos implementando un metodo de la interface
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        // findAll es un metodo de Spring que hace un SELECT * FROM clientes
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);
        // Si el estado es null, asignar 1 como valor por defecto
        if (cliente.getEstado() == null) {
            cliente.setEstado(1L);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(Long dpi) {
        // buscar un cliente por su DPI
        return clienteRepository.findById(dpi);
        // optional nos evita NullPointerException
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorEstado(int estado) {
        // Convertimos int a Long para comparar con el tipo del campo estado
        Long estadoLong = Long.valueOf(estado);
        return clienteRepository.findAll()
                .stream()
                .filter(c -> c.getEstado() != null && c.getEstado().equals(estadoLong))
                .collect(Collectors.toList());
        // devuelve todos los clientes que cumplen la condición
    }

    @Override
    public Cliente actualizar(Long dpi, Cliente cliente) {
        // Actualizar un cliente existente
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no encontrado con DPI " + dpi);
        }
        /*
         * 1. Asegura que el DPI del objeto coincida con el de la URL
         * 2. por seguridad usamos el DPI de la URL y no el que viene en el JSON
         */
        cliente.setDpiCliente(dpi);
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long dpi) {
        // Eliminar un cliente
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no encontrado con DPI " + dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(Long dpi) {
        // Verificar si existe el cliente
        return clienteRepository.existsById(dpi);
        // retornar true o false
    }

    // Metodo privado (solo puede utilizarse dentro de la clase)
    private void validarCliente(Cliente cliente) {
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         */
        if (cliente.getDpiCliente() == null) {
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }

        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }

        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }
    }
}