package com.cristianmanuel.Kinalapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto", nullable = false, columnDefinition = "INT")
    private Long codigoProducto;  // ID único del producto

    @Column(name = "nombre_producto", length = 60, nullable = false)
    private String nombreProducto;  // Nombre del producto

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;  // Precio actual del producto

    @Column(nullable = false, columnDefinition = "INT")
    private Long stock;  // Cantidad disponible en inventario

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;  // 1 = activo, 0 = inactivo

    public Producto() {}

    public Producto(Long codigoProducto, String nombreProducto, BigDecimal precio, Long stock, Long estado) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    // Getters y Setters
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
