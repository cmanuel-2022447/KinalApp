package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DetalleVenta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_detalle_venta", nullable = false, columnDefinition = "INT")
    private Long codigoDetalleVenta;

    @Column(nullable = false, columnDefinition = "INT")
    private Long cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;

    @ManyToOne
    @JoinColumn(name = "productos_codigo_producto", referencedColumnName = "codigo_producto", nullable = false)
    @JsonIgnoreProperties({"nombreProducto", "precio", "stock", "estado"})
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ventas_codigo_venta", referencedColumnName = "codigo_venta", nullable = false)
    @JsonIgnoreProperties({"fechaVenta", "total", "estado", "cliente", "usuario", "detallesVenta"})
    private Ventas venta;

    public DetalleVenta() {}

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