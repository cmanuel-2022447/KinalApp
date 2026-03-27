package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.DetalleVenta;
import com.cristianmanuel.Kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Anotación que registra un bean
 * Que la clase contiene la logica del negocio
 * */
@Service
/*
 * Por defecto, todos los metodos de esta clase seran transaccionales
 * Una Transaccion es que puede o no ocurrir algo
 * */
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    /*
     * private: solo es accesible dentro de la misma clase
     * final: no puede cambiar, es constante
     * DetalleVentaRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya q spring nos da el repositorio
     * */
    private final DetalleVentaRepository detalleVentaRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parametros: Spring pasa el repositorio automaticamente y a esto se le conoce
     * como Inyección de Dependencias
     * */
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    /*
     * @Override: Indica que estamos implementando un metodo de la interfaz
     * */
    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        // find all es un metodo de spring q hace un select * from detalleventa
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);
        // Si el estado es null, asignar 1 como valor por defecto
        if (detalleVenta.getEstado() == null) {
            detalleVenta.setEstado(1);
        }
        // Calcular subtotal si no viene
        if (detalleVenta.getSubtotal() == null && detalleVenta.getCantidad() > 0 && detalleVenta.getPrecioUnitario() != null) {
            detalleVenta.setSubtotal(detalleVenta.getPrecioUnitario().multiply(new java.math.BigDecimal(detalleVenta.getCantidad())));
        }
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(Integer codigo) {
        // buscar un detalle por su código
        return detalleVentaRepository.findById(codigo);
        // optional nos evita NullPointerException
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorEstado(int estado) {
        return detalleVentaRepository.findAll()
                .stream()
                .filter(detalle -> detalle.getEstado() != null && detalle.getEstado() == estado)
                .collect(Collectors.toList()); // devuelve todos los detalles que cumplen la condición
    }

    @Override
    public DetalleVenta actualizar(Integer codigo, DetalleVenta detalleVenta) {
        // Actualizar un detalle existente
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("DetalleVenta no se encontro con CODIGO " + codigo);
        }

        /*
         * 1. Asegura que el código del objeto coincida con el de la URL
         * 2. por seguridad usamos el código de la URL y no el que viene en el JSON
         * */
        detalleVenta.setCodigoDetalleVenta(codigo);
        validarDetalleVenta(detalleVenta);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(Integer codigo) {
        // Eliminar un detalle
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("El detalle no se encontro con el CODIGO " + codigo);
        }
        detalleVentaRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        // Verificar si existe el detalle
        return detalleVentaRepository.existsById(codigo);
        // retornar true o false
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorVenta(Integer codigoVenta) {
        // buscar detalles por código de venta
        return detalleVentaRepository.findAll()
                .stream()
                .filter(detalle -> detalle.getVenta() != null && detalle.getVenta().getCodigoVenta().equals(codigoVenta))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorProducto(Integer codigoProducto) {
        // buscar detalles por código de producto
        return detalleVentaRepository.findAll()
                .stream()
                .filter(detalle -> detalle.getProducto() != null && detalle.getProducto().getCodigoProducto().equals(codigoProducto))
                .collect(Collectors.toList());
    }

    // Metodo privado (solo puede utilizarse dentro de la clase)
    private void validarDetalleVenta(DetalleVenta detalleVenta) {
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */
        if (detalleVenta.getCodigoDetalleVenta() == null) {
            throw new IllegalArgumentException("El código del detalle es obligatorio");
        }

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