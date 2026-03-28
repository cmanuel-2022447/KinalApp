package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
// @Entity le dice a Spring que esta clase representa una tabla en la BD
@Table(name = "DetalleVenta")
// @Table define el nombre exacto de la tabla en la BD
public class DetalleVenta {

    @Id
    // @Id indica que este campo es la llave primaria de la tabla
    @Column(name = "codigo_detalle_venta", nullable = false, columnDefinition = "INT")
    // nullable = false: el campo no puede ser nulo en la BD
    // columnDefinition = "INT": define el tipo de dato en la BD
    private Long codigoDetalleVenta;

    @Column(nullable = false, columnDefinition = "INT")
    private Long cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    // precision = 10: numero total de digitos
    // scale = 2: numero de decimales
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;

    @ManyToOne
    // @ManyToOne: Muchos detalles pueden pertenecer a un solo Producto
    @JoinColumn(name = "productos_codigo_producto", referencedColumnName = "codigo_producto", nullable = false)
    // @JoinColumn: define la columna que hace la relacion con la tabla Productos
    @JsonIgnoreProperties({"nombreProducto", "precio", "stock", "estado"})
    // @JsonIgnoreProperties: evita mostrar estos campos del Producto en la respuesta JSON
    private Producto producto;

    @ManyToOne
    // @ManyToOne: Muchos detalles pueden pertenecer a una sola Venta
    @JoinColumn(name = "ventas_codigo_venta", referencedColumnName = "codigo_venta", nullable = false)
    // @JoinColumn: define la columna que hace la relacion con la tabla Ventas
    @JsonIgnoreProperties({"fechaVenta", "total", "estado", "cliente", "usuario", "detallesVenta"})
    // @JsonIgnoreProperties: evita mostrar estos campos de la Venta en la respuesta JSON
    private Ventas venta;

    // Constructor vacio requerido por JPA
    public DetalleVenta() {}

    // Constructor con todos los campos
    public DetalleVenta(Long codigoDetalleVenta, Long cantidad, BigDecimal precioUnitario,
                        BigDecimal subtotal, Long estado, Producto producto, Ventas venta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.estado = estado;
        this.producto = producto;
        this.venta = venta;
    }

    // Getters y Setters
    // Permiten acceder y modificar los atributos privados de la clase
    public Long getCodigoDetalleVenta() { return codigoDetalleVenta; }
    public void setCodigoDetalleVenta(Long codigoDetalleVenta) { this.codigoDetalleVenta = codigoDetalleVenta; }

    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Long getEstado() { return estado; }
    public void setEstado(Long estado) { this.estado = estado; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Ventas getVenta() { return venta; }
    public void setVenta(Ventas venta) { this.venta = venta; }
}