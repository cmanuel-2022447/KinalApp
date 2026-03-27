package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DetalleVenta")
public class DetalleVenta {

    @Id
    @Column(name = "codigo_detalle_venta", nullable = false)
    private Integer codigoDetalleVenta;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private Integer estado;

    @ManyToOne
    @JoinColumn(name = "productos_codigo_producto", referencedColumnName = "codigo_producto", nullable = false)
    @JsonIgnoreProperties({"detallesVenta", "nombreProducto", "precio", "stock", "estado"})
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ventas_codigo_venta", referencedColumnName = "codigo_venta", nullable = false)
    @JsonIgnoreProperties({"detallesVenta", "fechaVenta", "total", "estado", "cliente", "usuario"})
    private Ventas venta;

    // Constructores
    public DetalleVenta() {}

    public DetalleVenta(Integer codigoDetalleVenta, Integer cantidad, BigDecimal precioUnitario,
                        BigDecimal subtotal, Integer estado, Producto producto, Ventas venta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.estado = estado;
        this.producto = producto;
        this.venta = venta;
    }

    // Getters y Setters
    public Integer getCodigoDetalleVenta() { return codigoDetalleVenta; }
    public void setCodigoDetalleVenta(Integer codigoDetalleVenta) { this.codigoDetalleVenta = codigoDetalleVenta; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Ventas getVenta() { return venta; }
    public void setVenta(Ventas venta) { this.venta = venta; }
}