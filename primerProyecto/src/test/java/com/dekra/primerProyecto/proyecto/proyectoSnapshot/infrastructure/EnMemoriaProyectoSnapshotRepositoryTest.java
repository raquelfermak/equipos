package com.dekra.primerProyecto.proyecto.proyectoSnapshot.infrastructure;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.shared.id.IDValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnMemoriaProyectoSnapshotRepositoryTest {

    private EnMemoriaProyectoSnapshotRepository enMemoriaProyectoSnapshotRepository;

    @BeforeEach
    public void setup() {
        enMemoriaProyectoSnapshotRepository = new EnMemoriaProyectoSnapshotRepository();
    }

    @Test
    public void guardar_deberiaGuardarProyectoSnapshotCorrectamente() {
        // GIVEN
        ProyectoSnapshot snapshot = mock(ProyectoSnapshot.class);
        IDValue idMock = mock(IDValue.class);
        when(idMock.getValor()).thenReturn("id123");
        when(snapshot.getId()).thenReturn(idMock);
        when(snapshot.getLocalDateTime()).thenReturn(LocalDateTime.now());

        // WHEN
        enMemoriaProyectoSnapshotRepository.guardar(snapshot);

        // THEN
        List<ProyectoSnapshot> snapshots = enMemoriaProyectoSnapshotRepository.listar();
        assertEquals(1, snapshots.size(), "Se esperaba que el repositorio contenga un snapshot");
        assertEquals(snapshot, snapshots.get(0), "El snapshot guardado no coincide con el esperado");
    }

    @Test
    public void listar_deberiaListarTodosLosProyectoSnapshotsCorrectamente() {
        // GIVEN
        ProyectoSnapshot snapshot1 = mock(ProyectoSnapshot.class);
        ProyectoSnapshot snapshot2 = mock(ProyectoSnapshot.class);
        IDValue idMock1 = mock(IDValue.class);
        IDValue idMock2 = mock(IDValue.class);
        when(idMock1.getValor()).thenReturn("id123");
        when(idMock2.getValor()).thenReturn("id456");
        when(snapshot1.getId()).thenReturn(idMock1);
        when(snapshot2.getId()).thenReturn(idMock2);
        when(snapshot1.getLocalDateTime()).thenReturn(LocalDateTime.now());
        when(snapshot2.getLocalDateTime()).thenReturn(LocalDateTime.now());

        // WHEN
        enMemoriaProyectoSnapshotRepository.guardar(snapshot1);
        enMemoriaProyectoSnapshotRepository.guardar(snapshot2);

        // THEN
        List<ProyectoSnapshot> snapshots = enMemoriaProyectoSnapshotRepository.listar();
        assertEquals(2, snapshots.size(), "Se esperaba que el repositorio contenga dos snapshots");
        assertTrue(snapshots.contains(snapshot1), "El repositorio no contiene snapshot1");
        assertTrue(snapshots.contains(snapshot2), "El repositorio no contiene snapshot2");
    }

    @Test
    public void buscarUltimoPorId_deberiaRetornarElSnapshotMasRecienteSiExiste() {
        // GIVEN
        ProyectoSnapshot snapshotOld = mock(ProyectoSnapshot.class);
        ProyectoSnapshot snapshotNew = mock(ProyectoSnapshot.class);
        IDValue idMock = mock(IDValue.class);
        when(idMock.getValor()).thenReturn("id123");
        when(snapshotOld.getId()).thenReturn(idMock);
        when(snapshotNew.getId()).thenReturn(idMock);
        LocalDateTime timeOld = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime timeNew = LocalDateTime.of(2025, 1, 1, 12, 0);
        when(snapshotOld.getLocalDateTime()).thenReturn(timeOld);
        when(snapshotNew.getLocalDateTime()).thenReturn(timeNew);

        // WHEN
        enMemoriaProyectoSnapshotRepository.guardar(snapshotOld);
        enMemoriaProyectoSnapshotRepository.guardar(snapshotNew);

        // THEN
        ProyectoSnapshot resultado = enMemoriaProyectoSnapshotRepository.buscarUltimoPorId("id123");
        assertNotNull(resultado, "Se esperaba un snapshot con el id especificado");
        assertEquals(timeNew, resultado.getLocalDateTime(), "El snapshot retornado no es el más reciente");
    }

    @Test
    public void buscarUltimoPorId_deberiaRetornarNullSiNoExisteSnapshotConElId() {
        // GIVEN
        ProyectoSnapshot snapshot = mock(ProyectoSnapshot.class);
        IDValue idMock = mock(IDValue.class);
        when(idMock.getValor()).thenReturn("id123");
        when(snapshot.getId()).thenReturn(idMock);
        when(snapshot.getLocalDateTime()).thenReturn(LocalDateTime.now());
        enMemoriaProyectoSnapshotRepository.guardar(snapshot);

        // WHEN
        ProyectoSnapshot resultado = enMemoriaProyectoSnapshotRepository.buscarUltimoPorId("id999");

        // THEN
        assertNull(resultado, "Se esperaba null al no encontrar un snapshot con el id especificado");
    }
}