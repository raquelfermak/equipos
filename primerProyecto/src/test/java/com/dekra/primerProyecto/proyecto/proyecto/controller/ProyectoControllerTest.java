package com.dekra.primerProyecto.proyecto.proyecto.controller;

import com.dekra.primerProyecto.proyecto.proyecto.API.controller.ProyectoController;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.AsignacionDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.application.AsignacionProyectoService;
import com.dekra.primerProyecto.proyecto.proyecto.application.CrearProyectoService;
import com.dekra.primerProyecto.proyecto.proyecto.application.DesasignacionProyectoService;
import com.dekra.primerProyecto.proyecto.proyecto.application.ListarProyectoService;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.infrastrucure.repository.EnMemoriaRolRepository;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoControllerTest {

    @Mock
    private CrearProyectoService crearProyectoService;

    @Mock
    private ListarProyectoService listarProyectoService;

    @Mock
    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @Mock
    private EnMemoriaUsuarioRepository enMemoriaUsuarioRepository;

    @Mock
    private EnMemoriaRolRepository enMemoriaRolRepository;

    @Mock
    private AsignacionProyectoService asignacionProyectoService;

    @Mock
    private DesasignacionProyectoService desasignacionProyectoService;

    @InjectMocks
    private ProyectoController proyectoController;

    private Proyecto proyecto;
    private Usuario usuario;
    private Rol rol;


    @Test
    void crearProyecto_deberiaRetornarProyectoCreadoCorrectamente() {
        // GIVEN
        CrearActualizarProyectoDto crearActualizarProyectoDto = new CrearActualizarProyectoDto("P1", "paco@example.com", "descripcion",
                "");

        ListarProyectoDto expectedResponse = new ListarProyectoDto( "P1", "paco@example.com", "descripcion");


        // WHEN
        when(crearProyectoService.crearProyecto(crearActualizarProyectoDto)).thenReturn(expectedResponse);
        ListarProyectoDto resultado = proyectoController.crearProyecto(crearActualizarProyectoDto);

        // THEN
        assertEquals(expectedResponse.getNombre(), resultado.getNombre());
        assertEquals(expectedResponse.getEmail(), resultado.getEmail());
        assertEquals(expectedResponse.getDescripcion(), resultado.getDescripcion());
        verify(crearProyectoService, times(1)).crearProyecto(crearActualizarProyectoDto);
    }

    @Test
    void listarProyectos_deberiaRetornarListaCorrecta() {
        // GIVEN
        List<ListarProyectoDto> expectedList = Arrays.asList(new ListarProyectoDto("P1", "paco@example.com", "descripcion"), new ListarProyectoDto("P2", "carlos@example.com", "descripcion"));
        proyectoController.crearProyecto(new CrearActualizarProyectoDto("P1", "paco@example.com", "descripcion", ""));
        proyectoController.crearProyecto(new CrearActualizarProyectoDto("P2", "carlos@example.com", "descripcion", ""));

        //WHEN
        when(listarProyectoService.listar()).thenReturn(expectedList);
        List<ListarProyectoDto> actualList = proyectoController.listarProyectos();

        //THEN
        assertEquals(2, actualList.size());
        assertEquals(expectedList.get(0).getNombre(), actualList.get(0).getNombre());
        assertEquals(expectedList.get(1).getNombre(), actualList.get(1).getNombre());
        verify(listarProyectoService, times(1)).listar();
    }

    @Test
    void asignarUsuarioARol_deberiaAsignarUsuarioAlRolCorrectamente() {
        // GIVEN
        proyecto = new Proyecto("Proyecto Test", EmailValue.of("owner@test.com"), "Proyecto de prueba");
        usuario = new Usuario("UsuarioTest", EmailValue.of("usuariotest@test.com"));
        rol = new Rol("RolTest");
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", "r1");

        Map<Rol, Set<Usuario>> asignacionesEsperadas = new HashMap<>();
        Set<Usuario> usuariosRol = new HashSet<>();
        usuariosRol.add(usuario);
        asignacionesEsperadas.put(rol, usuariosRol);
        proyecto.setAsignaciones(asignacionesEsperadas);

        ListarProyectoDto proyectoEsperado = ListarProyectoDto.toDto(proyecto);

        // WHEN
        when(asignacionProyectoService.asignarUsuarioARol(asignacionDto)).thenReturn(proyectoEsperado);

        ListarProyectoDto resultado = proyectoController.asignarUsuarioARol(asignacionDto);

        // THEN

        assertNotNull(proyecto.getAsignaciones(), "El mapa de asignaciones no debería ser nulo");

        assertTrue(proyecto.getAsignaciones().containsKey(rol), "El proyecto debe contener la clave del rol asignado");

        Set<Usuario> usuariosAsignados = proyecto.getAsignaciones().get(rol);
        assertNotNull(usuariosAsignados, "El set de usuarios asignados no debería ser nulo");
        assertTrue(usuariosAsignados.contains(usuario), "El usuario debe estar asignado al rol");

        assertNotNull(resultado, "El DTO resultante no debe ser nulo");
        assertEquals("Proyecto Test", resultado.getNombre(), "El nombre del proyecto en el DTO debe coincidir");
    }

    @Test
    void desasignarUsuarioARol_deberiaDesasignarUsuarioAlRolCorrectamente() {
        // GIVEN
        proyecto = new Proyecto("Proyecto Test", EmailValue.of("owner@test.com"), "Proyecto de prueba");
        usuario = new Usuario("UsuarioTest", EmailValue.of("usuariotest@test.com"));
        rol = new Rol("RolTest");
        AsignacionDto asignacionDto = new AsignacionDto("p1", "u1", "r1");

        Map<Rol, Set<Usuario>> asignacionesEsperadas = new HashMap<>();
        proyecto.setAsignaciones(asignacionesEsperadas);

        ListarProyectoDto proyectoEsperado = ListarProyectoDto.toDto(proyecto);

        // WHEN
        when(desasignacionProyectoService.desasignarUsuarioARol(asignacionDto)).thenReturn(proyectoEsperado);

        ListarProyectoDto resultado = proyectoController.desasignarUsuarioARol(asignacionDto);

        // THEN

        assertNotNull(proyecto.getAsignaciones(), "El mapa de asignaciones no debería ser nulo");

        assertNotNull(resultado, "El DTO resultante no debe ser nulo");
        assertEquals("Proyecto Test", resultado.getNombre(), "El nombre del proyecto en el DTO debe coincidir");
    }


}