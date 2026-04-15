package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Ventas")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_venta", nullable = false, columnDefinition = "INT")
    private Long codigoVenta;  // Número único de venta

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;  // Fecha en que se realizó la venta

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;  // Suma total de la venta (acumulado de los subtotales de detalles)

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;  // Estado de la venta (completada, anulada, etc.)

    // Relación muchos a uno con Cliente: una venta pertenece a un cliente
    @ManyToOne
    @JoinColumn(name = "clientes_dpi_cliente", referencedColumnName = "dpi_cliente", nullable = false)
    @JsonIgnoreProperties({"nombreCliente", "apellidoCliente", "direccion", "estado"})
    private Cliente cliente;

    // Relación muchos a uno con Usuario: una venta es registrada por un usuario
    @ManyToOne
    @JoinColumn(name = "usuarios_codigo_usuario", referencedColumnName = "codigo_usuario", nullable = false)
    @JsonIgnoreProperties({"username", "password", "email", "rol", "estado"})
    private Usuario usuario;

    // Relación uno a muchos con DetalleVenta: una venta tiene muchos detalles
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Evita la serialización de la lista de detalles para no generar ciclos
    private List<DetalleVenta> detallesVenta;

    public Ventas() {}

    public Ventas(Long codigoVenta, LocalDate fechaVenta, BigDecimal total, Long estado, Cliente cliente, Usuario usuario) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getCodigoVenta() { return codigoVenta; }
    public void setCodigoVenta(Long codigoVenta) { this.codigoVenta = codigoVenta; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Long getEstado() { return estado; }
    public void setEstado(Long estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetalleVenta> getDetallesVenta() { return detallesVenta; }
    public void setDetallesVenta(List<DetalleVenta> detallesVenta) { this.detallesVenta = detallesVenta; }
}
