package com.cristianmanuel.Kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "Clientes")
public class Cliente {

    @Id
    @Column(name = "dpi_cliente", nullable = false)
    private Integer dpiCliente;

    @Column(name = "nombre_cliente", length = 50, nullable = false)
    private String nombreCliente;

    @Column(name = "apellido_cliente", length = 50, nullable = false)
    private String apellidoCliente;

    @Column(length = 100, nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Integer estado;

    // Constructores
    public Cliente() {}

    public Cliente(Integer dpiCliente, String nombreCliente, String apellidoCliente, String direccion, Integer estado) {
        this.dpiCliente = dpiCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccion = direccion;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getDpiCliente() { return dpiCliente; }
    public void setDpiCliente(Integer dpiCliente) { this.dpiCliente = dpiCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }
}