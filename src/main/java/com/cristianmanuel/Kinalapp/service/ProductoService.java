package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Producto;
import com.cristianmanuel.Kinalapp.repository.ProductoRepository;
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
public class ProductoService implements IProductoService {

    /*
     * private: solo es accesible dentro de la misma clase
     * final: no puede cambiar, es constante
     * ProductoRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya q spring nos da el repositorio
     * */
    private final ProductoRepository productoRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parametros: Spring pasa el repositorio automaticamente y a esto se le conoce
     * como Inyección de Dependencias
     * */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /*
     * @Override: Indica que estamos implementando un metodo de la interfaz
     * */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        // find all es un metodo de spring q hace un select * from productos
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        // Si el estado es null, asignar 1 como valor por defecto
        if (producto.getEstado() == null) {
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(Integer codigo) {
        // buscar un producto por su código
        return productoRepository.findById(codigo);
        // optional nos evita NullPointerException
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorEstado(int estado) {
        return productoRepository.findAll()
                .stream()
                .filter(producto -> producto.getEstado() != null && producto.getEstado() == estado)
                .collect(Collectors.toList()); // devuelve todos los productos que cumplen la condición
    }

    @Override
    public Producto actualizar(Integer codigo, Producto producto) {
        // Actualizar un producto existente
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("Producto no se encontro con CODIGO " + codigo);
        }

        /*
         * 1. Asegura que el código del objeto coincida con el de la URL
         * 2. por seguridad usamos el código de la URL y no el que viene en el JSON
         * */
        producto.setCodigoProducto(codigo);
        validarProducto(producto);

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Integer codigo) {
        // Eliminar un producto
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto no se encontro con el CODIGO " + codigo);
        }
        productoRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        // Verificar si existe el producto
        return productoRepository.existsById(codigo);
        // retornar true o false
    }

    // Metodo privado (solo puede utilizarse dentro de la clase)
    private void validarProducto(Producto producto) {
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */
        if (producto.getCodigoProducto() == null) {
            throw new IllegalArgumentException("El código del producto es obligatorio");
        }

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