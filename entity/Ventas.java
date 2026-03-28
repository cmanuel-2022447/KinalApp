package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
// @Entity le dice a Spring que esta clase representa una tabla en la BD
@Table(name = "Ventas")
// @Table define el nombre exacto de la tabla en la BD
public class Ventas {

    @Id
    // @Id indica que este campo es la llave primaria de la tabla
    @Column(name = "codigo_venta", nullable = false, columnDefinition = "INT")
    // nullable = false: el campo no puede ser nulo en la BD
    // columnDefinition = "INT": define el tipo de dato en la BD
    private Long codigoVenta;

    @Column(name = "fecha_venta", nullable = false)
    // LocalDate: almacena solo la fecha sin hora (yyyy-MM-dd)
    private LocalDate fechaVenta;

    @Column(precision = 10, scale = 2, nullable = false)
    // precision = 10: numero total de digitos
    // scale = 2: numero de decimales
    private BigDecimal total;

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;

    // Relación con Cliente
    @ManyToOne
    // @ManyToOne: Muchas ventas pueden pertenecer a un solo Cliente
    @JoinColumn(name = "clientes_dpi_cliente", referencedColumnName = "dpi_cliente", nullable = false)
    // @JoinColumn: define la columna que hace la relacion con la tabla Clientes
    @JsonIgnoreProperties({"nombreCliente", "apellidoCliente", "direccion", "estado"})
    // @JsonIgnoreProperties: evita mostrar estos campos del Cliente en la respuesta JSON
    private Cliente cliente;

    // Relación con Usuario
    @ManyToOne
    // @ManyToOne: Muchas ventas pueden pertenecer a un solo Usuario
    @JoinColumn(name = "usuarios_codigo_usuario", referencedColumnName = "codigo_usuario", nullable = false)
    // @JoinColumn: define la columna que hace la relacion con la tabla Usuarios
    @JsonIgnoreProperties({"username", "password", "email", "rol", "estado"})
    // @JsonIgnoreProperties: evita mostrar estos campos del Usuario en la respuesta JSON
    private Usuario usuario;

    // Detalles de venta (no se muestran en la respuesta)
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    // @JsonIgnore: evita mostrar los detalles en la respuesta JSON para evitar recursividad
    private List<DetalleVenta> detallesVenta;

    // Constructor vacio requerido por JPA
    public Ventas() {}

    // Constructor con todos los campos
    public Ventas(Long codigoVenta, LocalDate fechaVenta, BigDecimal total, Long estado,
                  Cliente cliente, Usuario usuario) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    // Getters y Setters
    // Permiten acceder y modificar los atributos privados de la clase
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