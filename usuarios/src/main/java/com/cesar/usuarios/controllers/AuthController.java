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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            String accessToken = jwtUtil.gerarToken(request.getEmail(), 1000 * 60 * 15); // 15 min
            String refreshToken = jwtUtil.gerarToken(request.getEmail(), 1000L * 60 * 60 * 24 * 7); // 7 dias

            return ResponseEntity.ok(new AuthTokens(accessToken, refreshToken));
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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshHeader) {
        if (refreshHeader == null || !refreshHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token ausente ou mal formado");
        }

        String refreshToken = refreshHeader.substring(7);

        if (!jwtUtil.tokenValido(refreshToken)) {
            return ResponseEntity.status(403).body("Token inválido");
        }

        String email = jwtUtil.extrairEmail(refreshToken);
        String newAccessToken = jwtUtil.gerarToken(email, 1000 * 60 * 15); // 15 min

        return ResponseEntity.ok(new AuthTokens(newAccessToken, refreshToken));
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

    @Data
    static class AuthTokens {
        private final String accessToken;
        private final String refreshToken;
    }

}
