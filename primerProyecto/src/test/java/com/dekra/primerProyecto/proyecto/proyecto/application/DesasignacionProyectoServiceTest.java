package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.AsignacionDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.CrearProyectoSnapshotService;
import com.dekra.primerProyecto.rol.application.event.RolConsultaEvent;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.application.CrearLogService;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.dekra.primerProyecto.usuario.application.event.UsuarioConsultaEvent;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DesasignacionProyectoServiceTest {
    @Mock
    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @Mock
    private CrearProyectoSnapshotService crearProyectoSnapshotService;

    @Mock
    private CrearLogService crearLogService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DesasignacionProyectoService desasignacionProyectoService;

    private Proyecto proyecto;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        EmailValue emailValue1 = EmailValue.of("carlos@example.com");
        EmailValue emailValue2 = EmailValue.of("juan@example.com");

        proyecto = new Proyecto("Proyecto 1", emailValue1, "descripcion");

        usuario = new Usuario("Usuario1", emailValue2);

        rol = new Rol("Desarrollador");

        // Inicializamos asignaciones vacías (se utilizará para algunos tests)
        proyecto.setAsignaciones(new HashMap<>());
    }

    /**
     * Utilidad para "simular" la carga de usuario y rol
     * cuando se publican los eventos en el servicio.
     */
    private void simularEventos(String usuarioIdEsperado, boolean usuarioExiste,
                                String rolIdEsperado, boolean rolExiste) {

        doAnswer(invocation -> {
            Object event = invocation.getArgument(0);

            if (event instanceof UsuarioConsultaEvent) {
                UsuarioConsultaEvent usuarioEvent = (UsuarioConsultaEvent) event;
                // Si coincide el id, seteamos el usuario en el evento
                if (usuarioEvent.getUsuarioId().equals(usuarioIdEsperado)) {
                    usuarioEvent.setExiste(usuarioExiste);
                }
            }
            else if (event instanceof RolConsultaEvent) {
                RolConsultaEvent rolEvent = (RolConsultaEvent) event;
                // Si coincide el id, seteamos el rol en el evento
                if (rolEvent.getRolId().equals(rolIdEsperado)) {
                    rolEvent.setExiste(rolExiste);
                }
            }

            // Para que no haya return value
            return null;
        }).when(eventPublisher).publishEvent(any());
    }

    @Test
    void desasignarUsuarioARol_deberiaDesasignarUsuarioCorrectamente() {
        // GIVEN: Se asigna el usuario a un rol en el proyecto.
        Set<IDValue> usuariosAsignados = new HashSet<>();
        usuariosAsignados.add(usuario.getId());
        Map<IDValue, Set<IDValue>> asignaciones = new HashMap<>();
        asignaciones.put(rol.getId(), usuariosAsignados);
        proyecto.setAsignaciones(asignaciones);

        AsignacionDto asignacionDto = new AsignacionDto("p1", usuario.getId().getValor(), rol.getId().getValor(), "Comentario de desasignación");


        // WHEN
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos(usuario.getId().getValor(), true, rol.getId().getValor(), true);
        ListarProyectoDto resultado = desasignacionProyectoService.desasignarUsuarioARol(asignacionDto);

        // THEN
        // Verifica que se haya invocado la creación del snapshot y del log.
        verify(crearProyectoSnapshotService, times(1)).crearProyectoSnapshot(proyecto);
        verify(crearLogService, times(1))
                .crearLog(eq(proyecto.getId()), eq(usuario.getId()), eq(TipoOperacion.MOD_ASIGNACIONES), eq("Comentario de desasignación"));

        // Verifica que se guarde el proyecto actualizado.
        verify(enMemoriaProyectoRepository, times(1)).guardar(proyecto);

        // Verifica que se haya removido el usuario y, dado que era el único, se remueva la entrada completa.
        assertFalse(proyecto.getAsignaciones().containsKey(rol.getId()), "El rol no debería existir en las asignaciones luego de remover el único usuario");

        // Verifica el DTO retornado.
        assertNotNull(resultado, "El DTO no debe ser nulo");
        assertEquals("Proyecto 1", resultado.getNombre(), "El nombre del proyecto debe coincidir");
    }

    @Test
    void desasignarUsuarioARol_deberiaLanzarExceptionSiAlgunoNoExiste() {
        // Caso: Proyecto es nulo.
        AsignacionDto dto = new AsignacionDto("p1", "u1", "r1", "Comentario");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(null);
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", true, "r1", true);


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> desasignacionProyectoService.desasignarUsuarioARol(dto),
                "Debe lanzar excepción cuando el proyecto es nulo");
        assertEquals("Algún elemento no existe.", exception.getMessage());

        // Caso: Usuario es nulo.
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", false, "r1", true);

        exception = assertThrows(IllegalArgumentException.class,
                () -> desasignacionProyectoService.desasignarUsuarioARol(dto),
                "Debe lanzar excepción cuando el usuario es nulo");
        assertEquals("Algún elemento no existe.", exception.getMessage());

        // Caso: Rol es nulo.
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", true, "r1", false);

        exception = assertThrows(IllegalArgumentException.class,
                () -> desasignacionProyectoService.desasignarUsuarioARol(dto),
                "Debe lanzar excepción cuando el rol es nulo");
        assertEquals("Algún elemento no existe.", exception.getMessage());
    }

    @Test
    void desasignarUsuarioARol_deberiaLanzarExceptionSiNoHayAsignaciones() {
        // GIVEN: Proyecto sin asignaciones (mapa nulo).
        proyecto.setAsignaciones(null);
        AsignacionDto dto = new AsignacionDto("p1", "u1", "r1", "Comentario");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", true, "r1", true);


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> desasignacionProyectoService.desasignarUsuarioARol(dto),
                "Debe lanzar excepción cuando no existen asignaciones");
        assertEquals("No hay asignaciones", exception.getMessage());
    }

    @Test
    void desasignarUsuarioARol_deberiaLanzarExceptionSiRolNoExisteEnAsignaciones() {
        // GIVEN: El proyecto tiene asignaciones, pero el rol buscado no está presente.
        proyecto.setAsignaciones(new HashMap<>());
        AsignacionDto dto = new AsignacionDto("p1", "u1", "r1", "Comentario");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", true, "r1", true);


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> desasignacionProyectoService.desasignarUsuarioARol(dto),
                "Debe lanzar excepción cuando el rol no existe en el proyecto");
        assertEquals("No existe el rol en el proyecto", exception.getMessage());
    }

    @Test
    void desasignarUsuarioARol_deberiaLanzarExceptionSiUsuarioNoEstaAsignadoAlRol() {
        // GIVEN: El proyecto tiene asignaciones para el rol, pero el usuario no se encuentra asignado.
        Set<IDValue> usuariosAsignados = new HashSet<>();
        // Se asigna un usuario distinto.
        Usuario otroUsuario = new Usuario("Otro Usuario", null);
        usuariosAsignados.add(otroUsuario.getId());
        Map<IDValue, Set<IDValue>> asignaciones = new HashMap<>();
        asignaciones.put(rol.getId(), usuariosAsignados);
        proyecto.setAsignaciones(asignaciones);

        AsignacionDto dto = new AsignacionDto("p1", "u1", rol.getId().getValor(), "Comentario");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);
        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", true, rol.getId().getValor(), true);


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> desasignacionProyectoService.desasignarUsuarioARol(dto),
                "Debe lanzar excepción cuando el usuario no está asignado al rol");
        assertEquals("No existe el usuario en el proyecto", exception.getMessage());
    }
}