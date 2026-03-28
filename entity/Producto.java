package com.cristianmanuel.Kinalapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
// @Entity le dice a Spring que esta clase representa una tabla en la BD
@Table(name = "Productos")
// @Table define el nombre exacto de la tabla en la BD
public class Producto {

    @Id
    // @Id indica que este campo es la llave primaria de la tabla
    @Column(name = "codigo_producto", nullable = false, columnDefinition = "INT")
    // nullable = false: el campo no puede ser nulo en la BD
    // columnDefinition = "INT": define el tipo de dato en la BD
    private Long codigoProducto;

    @Column(name = "nombre_producto", length = 60, nullable = false)
    // length = 60: longitud maxima del campo en la BD
    private String nombreProducto;

    @Column(precision = 10, scale = 2, nullable = false)
    // precision = 10: numero total de digitos
    // scale = 2: numero de decimales
    private BigDecimal precio;

    @Column(nullable = false, columnDefinition = "INT")
    private Long stock;

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;

    // Constructor vacio requerido por JPA
    public Producto() {}

    // Constructor con todos los campos
    public Producto(Long codigoProducto, String nombreProducto, BigDecimal precio,
                    Long stock, Long estado) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    // Getters y Setters
    // Permiten acceder y modificar los atributos privados de la clase
    public Long getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(Long codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Long getStock() { return stock; }
    public void setStock(Long stock) { this.stock = stock; }

    public Long getEstado() { return estado; }
    public void setEstado(Long estado) { this.estado = estado; }
}