package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Anotación que registra un bean
 * Que la clase contiene la logica del negocio
 * */
@Service
/*
 * Por defecto, todos los metodos de esta clase seran transaccionales
 * Una Transaccion es que puede o no ocurrir algo
 * */
@Transactional
public class UsuarioService implements IUsuarioService {

    /*
     * private: solo es accesible dentro de la misma clase
     * final: no puede cambiar, es constante
     * UsuarioRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya q spring nos da el repositorio
     * */
    private final UsuarioRepository usuarioRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parametros: Spring pasa el repositorio automaticamente y a esto se le conoce
     * como Inyección de Dependencias
     * */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * @Override: Indica que estamos implementando un metodo de la interfaz
     * */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        // find all es un metodo de spring q hace un select * from usuarios
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        // Si el estado es null, asignar 1 como valor por defecto
        if (usuario.getEstado() == null) {
            usuario.setEstado(1);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Integer codigo) {
        // buscar un usuario por su código
        return usuarioRepository.findById(codigo);
        // optional nos evita NullPointerException
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorEstado(int estado) {
        return usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getEstado() != null && usuario.getEstado() == estado)
                .collect(Collectors.toList()); // devuelve todos los usuarios que cumplen la condición
    }

    @Override
    public Usuario actualizar(Integer codigo, Usuario usuario) {
        // Actualizar un usuario existente
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no se encontro con CODIGO " + codigo);
        }

        /*
         * 1. Asegura que el código del objeto coincida con el de la URL
         * 2. por seguridad usamos el código de la URL y no el que viene en el JSON
         * */
        usuario.setCodigoUsuario(codigo);
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Integer codigo) {
        // Eliminar un usuario
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("El usuario no se encontro con el CODIGO " + codigo);
        }
        usuarioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        // Verificar si existe el usuario
        return usuarioRepository.existsById(codigo);
        // retornar true o false
    }

    // Metodo privado (solo puede utilizarse dentro de la clase)
    private void validarUsuario(Usuario usuario) {
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */
        if (usuario.getCodigoUsuario() == null) {
            throw new IllegalArgumentException("El código de usuario es obligatorio");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }
}