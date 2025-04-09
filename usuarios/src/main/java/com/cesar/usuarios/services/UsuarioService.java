package com.cesar.usuarios.services;

import com.cesar.usuarios.dtos.UsuarioDTO;
import com.cesar.usuarios.entities.Usuario;
import com.cesar.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public UsuarioDTO salvar(Usuario usuario) {
        repository.findByEmail(usuario.getEmail()).ifPresent(existingUser -> {
            throw new IllegalArgumentException("E-mail já está em uso");
        });

        // Criptografar a senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return toDTO(repository.save(usuario));
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPapel());
    }
}
