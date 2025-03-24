package com.dekra.primerProyecto.proyecto.proyectoSnapshot.API;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.dto.ListarProyectoSnapshotDto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.version.VersionValue;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

class ListarProyectoSnapshotDtoTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDto_deberiaRetornarNullSiProyectoSnapshotEsNull() {
        // GIVEN
        ProyectoSnapshot proyectoSnapshot = null;
        // WHEN
        ListarProyectoSnapshotDto dto = ListarProyectoSnapshotDto.toDto(proyectoSnapshot);
        // THEN
        assertNull(dto, "El DTO debe ser nulo cuando el proyecto snapshot es null");
    }

    @Test
    void toDto_deberiaConvertirProyectoSnapshotSinAsignaciones() {
        // GIVEN
        ProyectoSnapshot proyectoSnapshot = mock(ProyectoSnapshot.class);
        IDValue idMock = mock(IDValue.class);
        when(idMock.getValor()).thenReturn("id123");
        String nombre = "Proyecto Test";
        String descripcion = "Descripción de prueba";
        EmailValue emailMock= mock(EmailValue.class);
        when(emailMock.getValor()).thenReturn("carlos@example.com");
        VersionValue version = VersionValue.of(2);
        LocalDateTime fechaCreacion = LocalDateTime.now();

        when(proyectoSnapshot.getId()).thenReturn(idMock);
        when(proyectoSnapshot.getNombre()).thenReturn(nombre);
        when(proyectoSnapshot.getDescripcion()).thenReturn(descripcion);
        when(proyectoSnapshot.getEmail()).thenReturn(emailMock);
        when(proyectoSnapshot.getAsignaciones()).thenReturn(null);
        when(proyectoSnapshot.getVersionValue()).thenReturn(version);
        when(proyectoSnapshot.getLocalDateTime()).thenReturn(fechaCreacion);

        // WHEN
        ListarProyectoSnapshotDto dto = ListarProyectoSnapshotDto.toDto(proyectoSnapshot);

        // THEN
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertEquals(idMock.getValor(), dto.getId(), "El id debe coincidir");
        assertEquals(nombre, dto.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion, dto.getDescripcion(), "La descripción debe coincidir");
        assertEquals(emailMock.getValor(), dto.getEmail(), "El email debe coincidir");
        assertNull(dto.getAsignaciones(), "Las asignaciones deben ser nulas cuando no se proporcionan");
        assertEquals(version.getValor(), dto.getVersion(), "La versión debe coincidir");
        assertEquals(fechaCreacion, dto.getFechaCreacion(), "La fecha de creación debe coincidir");
    }

    @Test
    void toDto_deberiaConvertirProyectoSnapshotConAsignaciones() {
        // GIVEN
        ProyectoSnapshot proyectoSnapshot = mock(ProyectoSnapshot.class);
        IDValue id = IDValue.of();
        String nombre = "Proyecto Test";
        String descripcion = "Descripción de prueba";
        EmailValue email = EmailValue.of("carlos@example.com");
        VersionValue version = VersionValue.of(3);
        LocalDateTime fechaCreacion = LocalDateTime.now();

        // Creamos asignaciones con 1 entrada: un Rol y un Set de 1 Usuario.
        Rol rol = new Rol("Desarrollador");
        Usuario usuario = new Usuario("Juan", EmailValue.of("juan@example.com"));
        Map<IDValue, Set<IDValue>> asignaciones = new HashMap<>();
        asignaciones.put(rol.getId(), new HashSet<>(Collections.singletonList(usuario.getId())));

        when(proyectoSnapshot.getId()).thenReturn(id);
        when(proyectoSnapshot.getNombre()).thenReturn(nombre);
        when(proyectoSnapshot.getDescripcion()).thenReturn(descripcion);
        when(proyectoSnapshot.getEmail()).thenReturn(email);
        when(proyectoSnapshot.getAsignaciones()).thenReturn(asignaciones);
        when(proyectoSnapshot.getVersionValue()).thenReturn(version);
        when(proyectoSnapshot.getLocalDateTime()).thenReturn(fechaCreacion);

        // WHEN
        ListarProyectoSnapshotDto dto = ListarProyectoSnapshotDto.toDto(proyectoSnapshot);

        // THEN
        assertNotNull(dto, "El DTO no debe ser nulo");
        assertEquals(id.getValor(), dto.getId(), "El id debe coincidir");
        assertEquals(nombre, dto.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion, dto.getDescripcion(), "La descripción debe coincidir");
        assertEquals(email.getValor(), dto.getEmail(), "El email debe coincidir");
        assertNotNull(dto.getAsignaciones(), "Las asignaciones no deben ser nulas");
        assertEquals(version.getValor(), dto.getVersion(), "La versión debe coincidir");
        assertEquals(fechaCreacion, dto.getFechaCreacion(), "La fecha de creación debe coincidir");


        assertEquals(1, dto.getAsignaciones().size(), "Debe existir 1 entrada en el mapa de asignaciones");
        // Obtenemos la entrada y verificamos que tanto la clave (RolDto) como el conjunto de UsuarioDto no sean nulos.
        Map.Entry<String, Set<String>> entry = dto.getAsignaciones().entrySet().iterator().next();
        assertNotNull(entry.getKey(), "La clave del mapa (RolDto) no debe ser nula");
        assertNotNull(entry.getValue(), "El valor del mapa (Set<UsuarioDto>) no debe ser nulo");
        assertEquals(1, entry.getValue().size(), "El set de usuarios debe contener 1 elemento");
    }

}