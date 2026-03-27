package com.cristianmanuel.Kinalapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
public class Producto {

    @Id
    @Column(name = "codigo_producto", nullable = false)
    private Integer codigoProducto;

    @Column(name = "nombre_producto", length = 60, nullable = false)
    private String nombreProducto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer estado;

    // Constructores
    public Producto() {}

    public Producto(Integer codigoProducto, String nombreProducto, BigDecimal precio, Integer stock, Integer estado) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(Integer codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }
}