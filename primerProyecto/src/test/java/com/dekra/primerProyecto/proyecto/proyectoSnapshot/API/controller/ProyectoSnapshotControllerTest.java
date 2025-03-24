package com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.controller;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.dto.ListarProyectoSnapshotDto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.ListarProyectoSnapshotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoSnapshotControllerTest {

    @Mock
    private ListarProyectoSnapshotService listarProyectoSnapshotService;

    @InjectMocks
    private ProyectoSnapshotController proyectoSnapshotController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void listarProyectoSnapshots_deberiaRetornarListaCorrecta(){
        // GIVEN
        List<ListarProyectoSnapshotDto> listaEsperada = Arrays.asList(
                new ListarProyectoSnapshotDto("p1", "Proyecto prueba", "owner@test.com", "descripcion1", null, 0, LocalDateTime.now()),
                new ListarProyectoSnapshotDto("p1", "Proyecto prueba", "owner2@test.com", "descripcion2", null, 1, LocalDateTime.now()));

        // WHEN
        when(listarProyectoSnapshotService.listar()).thenReturn(listaEsperada);
        List<ListarProyectoSnapshotDto> resultado = proyectoSnapshotController.listarProyectoSnapshots();

        // THEN
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada.get(0).getNombre(), resultado.get(0).getNombre());
        assertEquals(listaEsperada.get(1).getNombre(), resultado.get(1).getNombre());
        assertEquals(listaEsperada.get(0).getEmail(), resultado.get(0).getEmail());
        assertEquals(listaEsperada.get(1).getEmail(), resultado.get(1).getEmail());
        assertEquals(listaEsperada.get(0).getDescripcion(), resultado.get(0).getDescripcion());
        assertEquals(listaEsperada.get(1).getDescripcion(), resultado.get(1).getDescripcion());

    }
}