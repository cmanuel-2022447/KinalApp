package com.cristianmanuel.Kinalapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_usuario", nullable = false, columnDefinition = "INT")
    private Long codigoUsuario;  // Identificador del usuario

    @Column(length = 45, nullable = false)
    private String username;  // Nombre de usuario para login

    @Column(length = 255, nullable = false)
    private String password;  // Contraseña (debería ir encriptada)

    @Column(length = 60, nullable = false)
    private String email;  // Correo electrónico

    @Column(length = 45, nullable = false)
    private String rol;  // Rol: ADMIN, VENDEDOR, etc.

    @Column(nullable = false, columnDefinition = "INT")
    private Long estado;  // 1 = activo, 0 = inactivo

    public Usuario() {}

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
