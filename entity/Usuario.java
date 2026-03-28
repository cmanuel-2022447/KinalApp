package com.cristianmanuel.Kinalapp.entity;

import jakarta.persistence.*;

@Entity
// @Entity le dice a Spring que esta clase representa una tabla en la BD
@Table(name = "Usuarios")
// @Table define el nombre exacto de la tabla en la BD
public class Usuario {

    @Id
    // @Id indica que este campo es la llave primaria de la tabla
    @Column(name = "codigo_usuario", nullable = false, columnDefinition = "INT")
    // nullable = false: el campo no puede ser nulo en la BD
    // columnDefinition = "INT": define el tipo de dato en la BD
    private Long codigoUsuario;

    @Column(length = 45, nullable = false)
    // length = 45: longitud maxima del campo en la BD
    private String username;

    @Column(length = 45, nullable = false)
    private String password;

    @Column(length = 60, nullable = false)
    // length = 60: longitud maxima del campo en la BD
    private String email;

    @Column(length = 45, nullable = false)
    private String rol;

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;

    // Constructor vacio requerido por JPA
    public Usuario() {}

    // Constructor con todos los campos
    public Usuario(Long codigoUsuario, String username, String password,
                   String email, String rol, Long estado) {
        this.codigoUsuario = codigoUsuario;
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y Setters
    // Permiten acceder y modificar los atributos privados de la clase
    public Long getCodigoUsuario() { return codigoUsuario; }
    public void setCodigoUsuario(Long codigoUsuario) { this.codigoUsuario = codigoUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Long getEstado() { return estado; }
    public void setEstado(Long estado) { this.estado = estado; }
}