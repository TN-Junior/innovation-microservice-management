package com.cesar.usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioServiceTest {
    @Test
    public void testPasswordHashing() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaBruta = "senha123";
        String senhaCriptografada = encoder.encode(senhaBruta);

        assertTrue(encoder.matches(senhaBruta, senhaCriptografada));
    }

}
