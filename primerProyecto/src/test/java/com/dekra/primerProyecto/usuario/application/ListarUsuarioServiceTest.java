package com.dekra.primerProyecto.usuario.application;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.application.service.ListarUsuarioService;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarUsuarioServiceTest {

    @Mock
    private EnMemoriaUsuarioRepository enMemoriaUsuarioRepository;

    @InjectMocks
    private ListarUsuarioService listarUsuarioService;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Creamos datos de prueba
        EmailValue emailValue1 = EmailValue.of("juan@example.com");
        EmailValue emailValue2 = EmailValue.of("maria@example.com");
        usuario1 = new Usuario("Juan", emailValue1);
        usuario2 = new Usuario("Maria", emailValue2);
    }

    @Test
    void listar_deberiaRetornarListaVaciaSiNoHayUsuariosEnElRepositorio() {
        // GIVEN / WHEN
        when(enMemoriaUsuarioRepository.listar()).thenReturn(Collections.emptyList());

        List<UsuarioDto> resultado = listarUsuarioService.listar();

        // THEN
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");
        verify(enMemoriaUsuarioRepository, times(1)).listar();
    }

    @Test
    void listar_deberiaRetornarListaDeUsuarioDtoCorrectamente() {
        // GIVEN / WHEN
        when(enMemoriaUsuarioRepository.listar()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioDto> resultado = listarUsuarioService.listar();

        // THEN
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería retornar 2 usuarios");

        // Verificamos contenido
        UsuarioDto dto1 = resultado.get(0);
        UsuarioDto dto2 = resultado.get(1);
        assertEquals(usuario1.getNombre(), dto1.getNombre());
        assertEquals(usuario1.getEmail().getValor(), dto1.getEmail());
        assertEquals(usuario2.getNombre(), dto2.getNombre());
        assertEquals(usuario2.getEmail().getValor(), dto2.getEmail());

        // Verificamos la llamada al mock
        verify(enMemoriaUsuarioRepository, times(1)).listar();
        // Aseguramos que no haya más interacciones con el mock
        verifyNoMoreInteractions(enMemoriaUsuarioRepository);
    }
}