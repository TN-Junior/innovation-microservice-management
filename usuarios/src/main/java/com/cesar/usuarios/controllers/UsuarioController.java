package com.cesar.usuarios.controllers;

import com.cesar.usuarios.dtos.UsuarioDTO;
import com.cesar.usuarios.entities.Usuario;
import com.cesar.usuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}



