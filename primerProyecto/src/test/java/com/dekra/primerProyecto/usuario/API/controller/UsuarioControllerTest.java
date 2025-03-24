package com.dekra.primerProyecto.usuario.API.controller;

import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.application.service.CrearUsuarioService;
import com.dekra.primerProyecto.usuario.application.service.ListarUsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private CrearUsuarioService crearUsuarioService;

    @Mock
    private ListarUsuarioService listarUsuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void crearUsuario_deberiaRetornarUsuarioDtoCreado() {
        // GIVEN
        UsuarioDto usuarioDtoEntrada = new UsuarioDto("Carlos", "carlos@example.com");
        UsuarioDto usuarioDtoSalida = new UsuarioDto("Carlos", "carlos@example.com");



        // WHEN
        when(crearUsuarioService.crearUsuario(usuarioDtoEntrada)).thenReturn(usuarioDtoSalida);
        UsuarioDto resultado = usuarioController.crearUsuario(usuarioDtoEntrada);

        // THEN
        assertEquals(usuarioDtoSalida, resultado,
                "El controlador debería retornar el UsuarioDto que regresa el servicio de creación");
        verify(crearUsuarioService, times(1)).crearUsuario(usuarioDtoEntrada);
    }

    @Test
    void listarUsuarios_deberiaRetornarListaDeUsuarios() {
        // GIVEN
        UsuarioDto usuario1 = new UsuarioDto("Juan", "juan@example.com");
        UsuarioDto usuario2 = new UsuarioDto("Maria", "maria@example.com");
        List<UsuarioDto> listaEsperada = Arrays.asList(usuario1, usuario2);



        // WHEN
        when(listarUsuarioService.listar()).thenReturn(listaEsperada);
        List<UsuarioDto> resultado = usuarioController.listarUsuarios();

        // THEN
        assertEquals(listaEsperada, resultado,
                "El controlador debería retornar la lista que devuelve el servicio de listado");
        verify(listarUsuarioService, times(1)).listar();
    }
}