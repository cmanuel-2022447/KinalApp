package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Cliente;
import com.cristianmanuel.Kinalapp.repository.ClienteRepository;
import com.cristianmanuel.Kinalapp.repository.VentasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service  // Registra esta clase como un bean de servicio en Spring
@Transactional  // Todas las operaciones de escritura se ejecutan dentro de una transacción
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final VentasRepository ventasRepository;

    // Inyección de dependencias por constructor
    public ClienteService(ClienteRepository clienteRepository, VentasRepository ventasRepository) {
        this.clienteRepository = clienteRepository;
        this.ventasRepository = ventasRepository;
    }

    @Override
    @Transactional(readOnly = true)  // Esta operación no modifica datos, solo lee
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();  // JpaRepository proporciona findAll()
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);  // Lanza excepción si faltan campos obligatorios
        if (cliente.getEstado() == null) {
            cliente.setEstado(1L);  // Por defecto, estado activo (1)
        }
        return clienteRepository.save(cliente);  // save inserta o actualiza según tenga ID o no
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(Long dpi) {
        return clienteRepository.findById(dpi);  // findById devuelve Optional
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorEstado(int estado) {
        Long estadoLong = Long.valueOf(estado);  // Convierte int a Long para comparar con campo de entidad
        // findAll() trae todos y se filtran en memoria con Stream API
        return clienteRepository.findAll().stream()
                .filter(c -> c.getEstado() != null && c.getEstado().equals(estadoLong))
                .collect(Collectors.toList());
    }

    @Override
    public Cliente actualizar(Long dpi, Cliente cliente) {
        // Verifica que el cliente exista antes de actualizar
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no encontrado con DPI " + dpi);
        }
        cliente.setDpiCliente(dpi);  // Asegura que el ID sea el correcto
        validarCliente(cliente);
        return clienteRepository.save(cliente);  // save actualiza porque el objeto tiene ID
    }

    @Override
    public void eliminar(Long dpi) {
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no encontrado con DPI " + dpi);
        }
        // Regla de negocio: no eliminar clientes con ventas asociadas (integridad referencial)
        boolean tieneVentas = ventasRepository.findAll().stream()
                .anyMatch(v -> v.getCliente() != null && v.getCliente().getDpiCliente().equals(dpi));
        if (tieneVentas) {
            throw new RuntimeException("No se puede eliminar el cliente porque tiene ventas registradas.");
        }
        clienteRepository.deleteById(dpi);  // Eliminación física
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(Long dpi) {
        return clienteRepository.existsById(dpi);
    }

    // Método privado de validación, reusable para guardar y actualizar
    private void validarCliente(Cliente cliente) {
        if (cliente.getDpiCliente() == null) {
            throw new IllegalArgumentException("El DPI es obligatorio");
        }
        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
    }
}