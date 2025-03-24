package com.dekra.primerProyecto.usuario.API.dto;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDtoTest {

    @Test
    public void testToDto_conUsuarioValido() {
        // GIVEN
        String nombreEsperado = "Juan Pérez";
        String emailEsperado = "juan.perez@example.com";
        EmailValue emailValue =  EmailValue.of(emailEsperado);
        Usuario usuario = new Usuario(nombreEsperado, emailValue);

        // WHEN
        UsuarioDto dto = UsuarioDto.toDto(usuario);

        // THEN
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertNotNull(dto.getId(), "El id no debe ser nulo");
        assertEquals(nombreEsperado, dto.getNombre(), "El nombre debe coincidir");
        assertEquals(emailEsperado, dto.getEmail(), "El email debe coincidir");
    }

    @Test
    public void testToDto_conUsuarioNull() {
        // GIVEN / WHEN
        UsuarioDto dto = UsuarioDto.toDto(null);

        // THEN
        assertNull(dto, "Al pasar null, el DTO debe ser nulo");
    }
}