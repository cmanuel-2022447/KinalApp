package com.cristianmanuel.Kinalapp.service;

import com.cristianmanuel.Kinalapp.entity.Usuario;
import com.cristianmanuel.Kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<Usuario> usuarios = usuarioRepository.findByUsername(username);

        if (usuarios.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Usuario usuario = usuarios.get(0);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol()) // Tu BD tiene "ADMIN", Security agrega "ROLE_" solo
                .build();
    }
}