package com.dekra.primerProyecto.proyecto.proyectoSnapshot.application;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.dto.ListarProyectoSnapshotDto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.infrastructure.EnMemoriaProyectoSnapshotRepository;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.version.VersionValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarProyectoSnapshotServiceTest {
    @Mock
    private EnMemoriaProyectoSnapshotRepository repository;

    @InjectMocks
    private ListarProyectoSnapshotService listarProyectoSnapshotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_deberiaRetornarListaDeSnapshotsConvertidosADto() {
        // GIVEN
        // Creamos dos instancias reales de ProyectoSnapshot con los datos requeridos.
        IDValue id1 = IDValue.of();
        EmailValue email = EmailValue.of("test@example.com");
        String nombre1 = "Proyecto 1";
        String descripcion1 = "Descripción 1";
        VersionValue version1 = VersionValue.of(1);
        ProyectoSnapshot snapshot1 = new ProyectoSnapshot(id1, nombre1, email, descripcion1, null, version1);

        IDValue id2 = IDValue.of();
        String nombre2 = "Proyecto 2";
        String descripcion2 = "Descripción 2";
        VersionValue version2 = VersionValue.of(2);
        ProyectoSnapshot snapshot2 = new ProyectoSnapshot(id2, nombre2, email, descripcion2, null, version2);

        List<ProyectoSnapshot> snapshots = Arrays.asList(snapshot1, snapshot2);

        // WHEN
        when(repository.listar()).thenReturn(snapshots);
        List<ListarProyectoSnapshotDto> dtos = listarProyectoSnapshotService.listar();

        // THEN
        assertNotNull(dtos, "La lista de DTOs no debe ser nula");
        assertEquals(2, dtos.size(), "Debe retornar 2 DTOs");

        // Se valida la conversión del primer snapshot
        ListarProyectoSnapshotDto dto1 = dtos.get(0);
        assertEquals(id1.getValor(), dto1.getId(), "El id debe coincidir");
        assertEquals(nombre1, dto1.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion1, dto1.getDescripcion(), "La descripción debe coincidir");
        assertEquals(email.getValor(), dto1.getEmail(), "El email debe coincidir");
        assertEquals(version1.getValor(), dto1.getVersion(), "La versión debe coincidir");
        // Se valida la conversión del segundo snapshot
        ListarProyectoSnapshotDto dto2 = dtos.get(1);
        assertEquals(id2.getValor(), dto2.getId(), "El id debe coincidir");
        assertEquals(nombre2, dto2.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion2, dto2.getDescripcion(), "La descripción debe coincidir");
        assertEquals(email.getValor(), dto2.getEmail(), "El email debe coincidir");
        assertEquals(version2.getValor(), dto2.getVersion(), "La versión debe coincidir");
    }
}