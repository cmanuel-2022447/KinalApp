package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity  // Indica que esta clase es una entidad JPA (se mapea a una tabla)
@Table(name = "DetalleVenta")  // Nombre de la tabla en la base de datos
public class DetalleVenta {

    @Id  // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_detalle_venta", nullable = false, columnDefinition = "INT")
    private Long codigoDetalleVenta;  // Identificador único del detalle de venta

    @Column(nullable = false, columnDefinition = "INT")
    private Long cantidad;  // Cantidad de productos vendidos en este detalle

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;  // Precio por unidad al momento de la venta

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;  // Calculado como cantidad * precioUnitario

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;  // Estado del detalle (activo, anulado, etc.)

    // Relación muchos a uno con Producto: cada detalle pertenece a un producto
    @ManyToOne
    @JoinColumn(name = "productos_codigo_producto", referencedColumnName = "codigo_producto", nullable = false)
    @JsonIgnoreProperties({"nombreProducto", "precio", "stock", "estado"})  // Evita bucles infinitos al serializar a JSON
    private Producto producto;

    // Relación muchos a uno con Ventas: cada detalle pertenece a una venta
    @ManyToOne
    @JoinColumn(name = "ventas_codigo_venta", referencedColumnName = "codigo_venta", nullable = false)
    @JsonIgnoreProperties({"fechaVenta", "total", "estado", "cliente", "usuario", "detallesVenta"})
    private Ventas venta;

    // Constructores
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

    // Getters y Setters
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
