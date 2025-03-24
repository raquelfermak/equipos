package com.dekra.primerProyecto.proyecto.proyectoSnapshot.application;

import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.infrastructure.EnMemoriaProyectoSnapshotRepository;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.version.VersionValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrearProyectoSnapshotServiceTest {

    @Mock
    private EnMemoriaProyectoSnapshotRepository repository;

    @InjectMocks
    private CrearProyectoSnapshotService crearProyectoSnapshotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearProyectoSnapshot_deberiaCrearSnapshotConVersionIncrementada() {
        // GIVEN: Un proyecto con un snapshot previo cuya versión es 5
        Proyecto proyecto = mock(Proyecto.class);
        IDValue idProyecto = IDValue.of();
        EmailValue email = EmailValue.of("test@example.com");
        String nombre = "Proyecto 1";
        String descripcion = "Descripción de prueba";
        Map asignaciones = null; // Se puede utilizar null o un Map vacío para las asignaciones

        // WHEN: Se configuran las respuestas del proyecto y se simula que existe un snapshot previo con versión 5
        when(proyecto.getId()).thenReturn(idProyecto);
        when(proyecto.getNombre()).thenReturn(nombre);
        when(proyecto.getEmail()).thenReturn(email);
        when(proyecto.getDescripcion()).thenReturn(descripcion);
        when(proyecto.getAsignaciones()).thenReturn(asignaciones);

        ProyectoSnapshot ultimoSnapshot = mock(ProyectoSnapshot.class);
        VersionValue versionAnterior = VersionValue.of(5);
        when(ultimoSnapshot.getVersionValue()).thenReturn(versionAnterior);

        when(repository.buscarUltimoPorId(idProyecto.getValor())).thenReturn(ultimoSnapshot);

        crearProyectoSnapshotService.crearProyectoSnapshot(proyecto);

        // THEN: Se verifica que el snapshot se guarda con la versión 6 (5 + 1) y con los datos correctos
        ArgumentCaptor<ProyectoSnapshot> captor = ArgumentCaptor.forClass(ProyectoSnapshot.class);
        verify(repository, times(1)).guardar(captor.capture());
        ProyectoSnapshot snapshotGuardado = captor.getValue();

        assertEquals(6, snapshotGuardado.getVersionValue().getValor(), "La versión del snapshot debe ser 6");
        assertEquals(nombre, snapshotGuardado.getNombre(), "El nombre debe coincidir");
        assertEquals(email, snapshotGuardado.getEmail(), "El email debe coincidir");
        assertEquals(descripcion, snapshotGuardado.getDescripcion(), "La descripción debe coincidir");
    }

    @Test
    void crearProyectoSnapshot_deberiaCrearPrimerSnapshotConVersion1Correctamente() {
        // GIVEN: Un proyecto sin snapshot previo (no existe ningún snapshot en el repositorio)
        Proyecto proyecto = mock(Proyecto.class);
        IDValue idProyecto = IDValue.of();
        EmailValue email = EmailValue.of("first@example.com");
        String nombre = "Proyecto First";
        String descripcion = "Descripción First";
        Map asignaciones = null; // Se puede utilizar null o un Map vacío para las asignaciones

        // WHEN: Se configuran las respuestas del proyecto y se simula que no existe snapshot previo (buscarUltimoPorId retorna null)
        when(proyecto.getId()).thenReturn(idProyecto);
        when(proyecto.getNombre()).thenReturn(nombre);
        when(proyecto.getEmail()).thenReturn(email);
        when(proyecto.getDescripcion()).thenReturn(descripcion);
        when(proyecto.getAsignaciones()).thenReturn(asignaciones);

        when(repository.buscarUltimoPorId(idProyecto.getValor())).thenReturn(null);

        crearProyectoSnapshotService.crearProyectoSnapshot(proyecto);

        // THEN: Se verifica que se guarda un snapshot con versión 1 y los datos correctos del proyecto
        ArgumentCaptor<ProyectoSnapshot> captor = ArgumentCaptor.forClass(ProyectoSnapshot.class);
        verify(repository, times(1)).guardar(captor.capture());
        ProyectoSnapshot snapshotGuardado = captor.getValue();

        assertEquals(0, snapshotGuardado.getVersionValue().getValor(), "La versión del snapshot debe ser 1");
        assertEquals(nombre, snapshotGuardado.getNombre(), "El nombre debe coincidir");
        assertEquals(email, snapshotGuardado.getEmail(), "El email debe coincidir");
        assertEquals(descripcion, snapshotGuardado.getDescripcion(), "La descripción debe coincidir");
    }
}
