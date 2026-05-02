package com.cristianmanuel.Kinalapp.entity;

import jakarta.persistence.*;

@Entity
// @Entity le dice a Spring que esta clase representa una tabla en la BD
@Table(name = "Clientes")
// @Table define el nombre exacto de la tabla en la BD
public class Cliente {

    @Id
    // @Id indica que este campo es la llave primaria de la tabla
    @Column(name = "dpi_cliente", nullable = false, columnDefinition = "INT")
    // nullable = false: el campo no puede ser nulo en la BD
    // columnDefinition = "INT": define el tipo de dato en la BD
    private Long dpiCliente;

    @Column(name = "nombre_cliente", length = 50, nullable = false)
    // length = 50: longitud maxima del campo en la BD
    private String nombreCliente;

    @Column(name = "apellido_cliente", length = 50, nullable = false)
    private String apellidoCliente;

    @Column(length = 100, nullable = false)
    private String direccion;

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;

    // Constructor vacio requerido por JPA
    public Cliente() {}

    // Constructor con todos los campos
    public Cliente(Long dpiCliente, String nombreCliente, String apellidoCliente,
                   String direccion, Long estado) {
        this.dpiCliente = dpiCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccion = direccion;
        this.estado = estado;
    }

    // Getters y Setters
    // Permiten acceder y modificar los atributos privados de la clase
    public Long getDpiCliente() { return dpiCliente; }
    public void setDpiCliente(Long dpiCliente) { this.dpiCliente = dpiCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Long getEstado() { return estado; }
    public void setEstado(Long estado) { this.estado = estado; }
}