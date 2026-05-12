package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.repository.ProductoRepository;
import com.cristianmanuel.Kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;  // Para verificar dependencias

    public ProductoService(ProductoRepository productoRepository, DetalleVentaRepository detalleVentaRepository) {
        this.productoRepository = productoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        if (producto.getEstado() == null) {
            producto.setEstado(1L);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(Long codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorEstado(int estado) {
        Long estadoLong = Long.valueOf(estado);
        return productoRepository.findAll().stream()
                .filter(p -> p.getEstado() != null && p.getEstado().equals(estadoLong))
                .collect(Collectors.toList());
    }

    @Override
    public Producto actualizar(Long codigo, Producto producto) {
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("Producto no encontrado con código " + codigo);
        }
        producto.setCodigoProducto(codigo);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("Producto no encontrado con código " + codigo);
        }
        // Impedir eliminación si el producto aparece en algún detalle de venta
        boolean tieneDetalles = detalleVentaRepository.findAll().stream()
                .anyMatch(d -> d.getProducto() != null && d.getProducto().getCodigoProducto().equals(codigo));
        if (tieneDetalles) {
            throw new RuntimeException("No se puede eliminar el producto porque tiene detalles de venta asociados.");
        }
        productoRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return productoRepository.existsById(codigo);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}