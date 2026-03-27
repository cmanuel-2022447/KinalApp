package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Ventas;
import com.cristianmanuel.Kinalapp.repository.VentasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
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
public class VentasService implements IVentasService {

    /*
     * private: solo es accesible dentro de la misma clase
     * final: no puede cambiar, es constante
     * VentasRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya q spring nos da el repositorio
     * */
    private final VentasRepository ventasRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parametros: Spring pasa el repositorio automaticamente y a esto se le conoce
     * como Inyección de Dependencias
     * */
    public VentasService(VentasRepository ventasRepository) {
        this.ventasRepository = ventasRepository;
    }

    /*
     * @Override: Indica que estamos implementando un metodo de la interfaz
     * */
    @Override
    @Transactional(readOnly = true)
    public List<Ventas> listarTodos() {
        // find all es un metodo de spring q hace un select * from ventas
        return ventasRepository.findAll();
    }

    @Override
    public Ventas guardar(Ventas ventas) {
        // Si no se proporciona fecha, asignar la actual
        if (ventas.getFechaVenta() == null) {
            ventas.setFechaVenta(LocalDate.now());
        }
        validarVentas(ventas);
        // Si el estado es null, asignar 1 como valor por defecto
        if (ventas.getEstado() == null) {
            ventas.setEstado(1);
        }
        return ventasRepository.save(ventas);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ventas> buscarPorCodigo(Integer codigo) {
        // buscar una venta por su código
        return ventasRepository.findById(codigo);
        // optional nos evita NullPointerException
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ventas> buscarPorEstado(int estado) {
        return ventasRepository.findAll()
                .stream()
                .filter(venta -> venta.getEstado() != null && venta.getEstado() == estado)
                .collect(Collectors.toList()); // devuelve todas las ventas que cumplen la condición
    }

    @Override
    public Ventas actualizar(Integer codigo, Ventas ventas) {
        // Actualizar una venta existente
        if (!ventasRepository.existsById(codigo)) {
            throw new RuntimeException("Venta no se encontro con CODIGO " + codigo);
        }

        /*
         * 1. Asegura que el código del objeto coincida con el de la URL
         * 2. por seguridad usamos el código de la URL y no el que viene en el JSON
         * */
        ventas.setCodigoVenta(codigo);
        validarVentas(ventas);

        return ventasRepository.save(ventas);
    }

    @Override
    public void eliminar(Integer codigo) {
        // Eliminar una venta
        if (!ventasRepository.existsById(codigo)) {
            throw new RuntimeException("La venta no se encontro con el CODIGO " + codigo);
        }
        ventasRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        // Verificar si existe la venta
        return ventasRepository.existsById(codigo);
        // retornar true o false
    }

    // Metodo privado (solo puede utilizarse dentro de la clase)
    private void validarVentas(Ventas venta) {
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */
        if (venta.getCodigoVenta() == null) {
            throw new IllegalArgumentException("El código de venta es obligatorio");
        }

        if (venta.getTotal() == null) {
            throw new IllegalArgumentException("El total de venta es obligatorio");
        }

        if (venta.getCliente() == null || venta.getCliente().getDpiCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (venta.getUsuario() == null || venta.getUsuario().getCodigoUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha de venta es obligatoria");
        }
    }
}