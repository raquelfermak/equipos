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
import com.dekra.primerProyecto.usuario.application.event.UsuarioConsultaEvent;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AsignacionProyectoServiceTest {

    @Mock
    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @Mock
    private CrearProyectoSnapshotService crearProyectoSnapshotService;

    @Mock
    private CrearLogService crearLogService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AsignacionProyectoService asignacionProyectoService;

    private Proyecto proyecto;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        // Inicializamos objetos de prueba
        EmailValue emailValue1 = EmailValue.of("carlos@example.com");
        EmailValue emailValue2 = EmailValue.of("juan@example.com");

        proyecto = new Proyecto("Proyecto 1", emailValue1, "descripcion");
        usuario = new Usuario("Juan", emailValue2);
        rol = new Rol("Desarrollador");
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
    void asignarUsuarioARol_deberiaAsignarUsuarioCuandoNoExisteEseRol() {
        // GIVEN
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", "r1");

        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);

        // Simulamos que, cuando se publiquen eventos para "u1" y "r1",
        // se va a setear 'usuario' y 'rol' respectivamente en dichos eventos
        simularEventos("u1", true, "r1", true);

        // WHEN
        ListarProyectoDto resultado = asignacionProyectoService.asignarUsuarioARol(asignacionDto);

        // THEN
        // Verifica que el repositorio guardó el proyecto
        verify(enMemoriaProyectoRepository, times(1)).guardar(proyecto);

        // Verifica que en el proyecto se creó un Map de asignaciones
        assertNotNull(resultado.getAsignaciones(), "El mapa de asignaciones no debería ser nulo");

        // Verifica el set de usuarios asignados
        Set<?> usuariosAsignados =
                resultado.getAsignaciones().entrySet().iterator().next().getValue();
        assertEquals(1, usuariosAsignados.size(),
                "El set de usuarios debe contener exactamente 1 elemento");

        // Verifica contenido del DTO de respuesta
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Proyecto 1", resultado.getNombre(),
                "El nombre del proyecto en el DTO debe coincidir");
    }

    @Test
    void asignarUsuarioARol_deberiaAsignarUsuarioCuandoYaExisteEseRol() {
        // GIVEN
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", rol.getId().getValor());

        // Proyecto ya tiene un rol con un usuario asignado
        EmailValue emailValue3 = EmailValue.of("maria@example.com");
        Usuario maria = new Usuario("Maria", emailValue3);

        Map<IDValue, Set<IDValue>> asignacionesExistentes = new HashMap<>();
        asignacionesExistentes.put(rol.getId(), new HashSet<>(Collections.singletonList(maria.getId())));
        proyecto.setAsignaciones(asignacionesExistentes);

        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);

        // Simulamos que los eventos devuelven 'usuario' y 'rol'
        simularEventos("u1", true, rol.getId().getValor(), true);

        // WHEN
        ListarProyectoDto resultado = asignacionProyectoService.asignarUsuarioARol(asignacionDto);

        // THEN
        verify(enMemoriaProyectoRepository, times(1)).guardar(proyecto);


        // Ahora el set debería tener 2 usuarios: 'Maria' y 'Juan'
        Set<?> usuariosAsignados =
                resultado.getAsignaciones().entrySet().iterator().next().getValue();
        assertEquals(2, usuariosAsignados.size(),
                "El set de usuarios debe contener 2 elementos");
    }

    @Test
    void asignarUsuarioARol_deberiaLanzarExceptionSiProyectoNoExiste() {
        // GIVEN
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", "r1");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(null);

        // Aunque simulemos aquí usuario y rol, el proyecto es nulo,
        // así que la excepción se lanzará antes.
        simularEventos("u1", true, "r1", true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class,
                () -> asignacionProyectoService.asignarUsuarioARol(asignacionDto),
                "Debe lanzar excepción si el proyecto no existe");

        verify(enMemoriaProyectoRepository, never()).guardar(any(Proyecto.class));
    }

    @Test
    void asignarUsuarioARol_deberiaLanzarExceptionSiUsuarioNoExiste() {
        // GIVEN
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", "r1");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);

        // Simulamos que el evento para "u1" no asigna ningún usuario => null
        simularEventos("u1", false, "r1", true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class,
                () -> asignacionProyectoService.asignarUsuarioARol(asignacionDto),
                "Debe lanzar excepción si el usuario no existe");

        verify(enMemoriaProyectoRepository, never()).guardar(any(Proyecto.class));
    }

    @Test
    void asignarUsuarioARol_deberiaLanzarExceptionSiRolNoExiste() {
        // GIVEN
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", "r1");
        when(enMemoriaProyectoRepository.buscarPorId("p1")).thenReturn(proyecto);

        // Simulamos que el evento para "r1" no asigna ningún rol => null
        simularEventos("u1", true, "r1", false);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class,
                () -> asignacionProyectoService.asignarUsuarioARol(asignacionDto),
                "Debe lanzar excepción si el rol no existe");

        verify(enMemoriaProyectoRepository, never()).guardar(any(Proyecto.class));
    }
}
