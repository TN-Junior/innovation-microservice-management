package com.cesar.usuarios.controllers;

import com.cesar.usuarios.dtos.UsuarioDTO;
import com.cesar.usuarios.entities.Usuario;
import com.cesar.usuarios.security.JwtUtil;
import com.cesar.usuarios.services.UsuarioService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            String token = jwtUtil.gerarToken(request.getEmail());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            UsuarioDTO novoUsuario = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @Data
    static class AuthRequest {
        @NotBlank
        private String email;
        @NotBlank
        private String senha;
    }

    @Data
    static class AuthResponse {
        private final String token;
    }
}
