package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.repository.UsuarioRepository;
import com.cristianmanuel.Kinalapp.repository.VentasRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VentasRepository ventasRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, VentasRepository ventasRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.ventasRepository = ventasRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarCampos(usuario);
        if (usuario.getEstado() == null) {
            usuario.setEstado(1L);
        }
        // Encriptar la contraseña con BCrypt antes de persistir
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorEstado(int estado) {
        Long estadoLong = Long.valueOf(estado);
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() != null && u.getEstado().equals(estadoLong))
                .collect(Collectors.toList());
    }

    @Override
    public Usuario actualizar(Long codigo, Usuario usuario) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código " + codigo);
        }
        usuario.setCodigoUsuario(codigo);
        validarCampos(usuario);
        // Encriptar la nueva contraseña con BCrypt antes de persistir
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código " + codigo);
        }
        // No permitir eliminar usuarios que tengan ventas asociadas
        boolean tieneVentas = ventasRepository.findAll().stream()
                .anyMatch(v -> v.getUsuario() != null && v.getUsuario().getCodigoUsuario().equals(codigo));
        if (tieneVentas) {
            throw new RuntimeException("No se puede eliminar el usuario porque tiene ventas registradas.");
        }
        usuarioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return usuarioRepository.existsById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> login(String username, String password) {
        List<Usuario> usuarios = usuarioRepository.findByUsername(username);
        // Comparar con BCrypt (no texto plano)
        return usuarios.stream()
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    private void validarCampos(Usuario usuario) {
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
