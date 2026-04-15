package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.DetalleVenta;
import com.cristianmanuel.Kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);
        if (detalleVenta.getEstado() == null) {
            detalleVenta.setEstado(1L);  // Estado activo por defecto
        }
        // Si el subtotal no fue enviado, se calcula automáticamente
        if (detalleVenta.getSubtotal() == null && detalleVenta.getCantidad() > 0 && detalleVenta.getPrecioUnitario() != null) {
            detalleVenta.setSubtotal(detalleVenta.getPrecioUnitario().multiply(new java.math.BigDecimal(detalleVenta.getCantidad())));
        }
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(Long codigo) {
        return detalleVentaRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorEstado(int estado) {
        Long estadoLong = Long.valueOf(estado);
        return detalleVentaRepository.findAll().stream()
                .filter(d -> d.getEstado() != null && d.getEstado().equals(estadoLong))
                .collect(Collectors.toList());
    }

    @Override
    public DetalleVenta actualizar(Long codigo, DetalleVenta detalleVenta) {
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("DetalleVenta no encontrado con código " + codigo);
        }
        detalleVenta.setCodigoDetalleVenta(codigo);  // Fija la clave primaria
        validarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("DetalleVenta no encontrado con código " + codigo);
        }
        detalleVentaRepository.deleteById(codigo);  // Eliminación física permitida
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return detalleVentaRepository.existsById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorVenta(Long codigoVenta) {
        // Filtra en memoria todos los detalles que pertenecen a la venta dada
        return detalleVentaRepository.findAll().stream()
                .filter(d -> d.getVenta() != null && d.getVenta().getCodigoVenta().equals(codigoVenta))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorProducto(Long codigoProducto) {
        // Filtra detalles que referencian el producto indicado
        return detalleVentaRepository.findAll().stream()
                .filter(d -> d.getProducto() != null && d.getProducto().getCodigoProducto().equals(codigoProducto))
                .collect(Collectors.toList());
    }

    // Validaciones comunes para evitar datos incorrectos
    private void validarDetalleVenta(DetalleVenta detalleVenta) {
        if (detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (detalleVenta.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("El precio unitario es obligatorio");
        }
        if (detalleVenta.getProducto() == null || detalleVenta.getProducto().getCodigoProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (detalleVenta.getVenta() == null || detalleVenta.getVenta().getCodigoVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
    }
}