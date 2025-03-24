package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.CrearProyectoSnapshotService;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.application.CrearLogService;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActualizarProyectoServiceTest {

    @Mock
    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @Mock
    private EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    @Mock
    private CrearProyectoSnapshotService crearProyectoSnapshotService;

    @Mock
    private CrearLogService crearLogService;

    @InjectMocks
    private ActualizarProyectoService actualizarProyectoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void actualizarProyecto_deberiaActualizarProyectoCorrectamente() {
        // GIVEN
        Proyecto proyecto = mock(Proyecto.class);
        IDValue idMock = mock(IDValue.class);
        when(idMock.getValor()).thenReturn("id123");
        when(proyecto.getId()).thenReturn(idMock);

        EmailValue emailMock = mock(EmailValue.class);
        when(emailMock.getValor()).thenReturn("viejo@example.com");
        when(proyecto.getEmail()).thenReturn(emailMock);

        CrearActualizarProyectoDto dto = new CrearActualizarProyectoDto();
        dto.setNombre("Nuevo Nombre");
        dto.setEmail("nuevo@example.com");
        dto.setDescripcion("Nueva descripción");
        dto.setComentario("Actualización realizada");

        // Se simula que el repositorio encuentra el proyecto dado el id
        when(enMemoriaProyectoRepository.buscarPorId("id123")).thenReturn(proyecto);

        // WHEN
        ListarProyectoDto resultado = actualizarProyectoService.actualizarProyecto("id123", dto);

        // THEN
        verify(crearProyectoSnapshotService, times(1)).crearProyectoSnapshot(proyecto);
        verify(proyecto, times(1)).setNombre("Nuevo Nombre");
        verify(proyecto, times(1)).setEmail(EmailValue.of("nuevo@example.com"));
        verify(proyecto, times(1)).setDescripcion("Nueva descripción");
        verify(enMemoriaProyectoRepository, times(1)).guardar(proyecto);
        verify(crearLogService, times(1)).crearLog(proyecto.getId(), null, TipoOperacion.MOD_ATRIBUTOS, "Actualización realizada");
        assertNotNull(resultado, "El DTO de proyecto actualizado no debe ser null");
    }

    @Test
    void actualizarProyecto_deberiaActualizarProyectoAtributosNulosCorrectamente() {
        // GIVEN
        Proyecto proyecto = mock(Proyecto.class);
        when(proyecto.getNombre()).thenReturn("Viejo nombre");
        IDValue idMock = mock(IDValue.class);
        when(idMock.getValor()).thenReturn("id123");
        when(proyecto.getId()).thenReturn(idMock);

        EmailValue emailMock = mock(EmailValue.class);
        when(emailMock.getValor()).thenReturn("viejo@example.com");
        when(proyecto.getEmail()).thenReturn(emailMock);

        CrearActualizarProyectoDto dto = new CrearActualizarProyectoDto();

        // Se simula que el repositorio encuentra el proyecto dado el id
        when(enMemoriaProyectoRepository.buscarPorId("id123")).thenReturn(proyecto);

        // WHEN
        ListarProyectoDto resultado = actualizarProyectoService.actualizarProyecto("id123", dto);

        // THEN
        verify(crearProyectoSnapshotService, times(1)).crearProyectoSnapshot(proyecto);
        verify(enMemoriaProyectoRepository, times(1)).guardar(proyecto);
        verify(crearLogService, times(1)).crearLog(proyecto.getId(), null, TipoOperacion.MOD_ATRIBUTOS, null);
        assertEquals(proyecto.getNombre(), resultado.getNombre());
        assertEquals(proyecto.getEmail().getValor(), resultado.getEmail());
        assertEquals(proyecto.getDescripcion(), resultado.getDescripcion());
        assertNotNull(resultado, "El DTO de proyecto actualizado no debe ser null");
    }



    @Test
    void actualizarProyecto_deberiaLanzarExcepcionSiNoExisteElProyecto() {
        // GIVEN
        String id = "proyectoInexistente";
        CrearActualizarProyectoDto dto = new CrearActualizarProyectoDto();
        dto.setNombre("Nombre");
        dto.setEmail("email@example.com");
        dto.setDescripcion("Descripción");
        dto.setComentario("Comentario");

        when(enMemoriaProyectoRepository.buscarPorId(id)).thenReturn(null);

        // WHEN & THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            actualizarProyectoService.actualizarProyecto(id, dto);
        });
        assertEquals("No existe ese proyecto", exception.getMessage(), "El mensaje de la excepción no coincide");
    }
}