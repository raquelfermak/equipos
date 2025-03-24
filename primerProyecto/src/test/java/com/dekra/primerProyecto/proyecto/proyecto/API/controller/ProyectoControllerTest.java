package com.dekra.primerProyecto.proyecto.proyecto.API.controller;

import com.dekra.primerProyecto.proyecto.proyecto.API.controller.ProyectoController;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.AsignacionDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.application.*;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
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
    private AsignacionProyectoService asignacionProyectoService;

    @Mock
    private DesasignacionProyectoService desasignacionProyectoService;

    @Mock
    private ActualizarProyectoService actualizarProyectoService;

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

        Map<IDValue, Set<IDValue>> asignacionesEsperadas = new HashMap<>();
        Set<IDValue> usuariosRol = new HashSet<>();
        usuariosRol.add(usuario.getId());
        asignacionesEsperadas.put(rol.getId(), usuariosRol);
        proyecto.setAsignaciones(asignacionesEsperadas);

        ListarProyectoDto proyectoEsperado = ListarProyectoDto.toDto(proyecto);

        // WHEN
        when(asignacionProyectoService.asignarUsuarioARol(asignacionDto)).thenReturn(proyectoEsperado);

        ListarProyectoDto resultado = proyectoController.asignarUsuarioARol(asignacionDto);

        // THEN

        assertNotNull(proyecto.getAsignaciones(), "El mapa de asignaciones no debería ser nulo");

        assertTrue(proyecto.getAsignaciones().containsKey(rol.getId()), "El proyecto debe contener la clave del rol asignado");

        Set<IDValue> usuariosAsignados = proyecto.getAsignaciones().get(rol.getId());
        assertNotNull(usuariosAsignados, "El set de usuarios asignados no debería ser nulo");
        assertTrue(usuariosAsignados.contains(usuario.getId()), "El usuario debe estar asignado al rol");

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

        Map<IDValue, Set<IDValue>> asignacionesEsperadas = new HashMap<>();
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


    @Test
    void actualizarProyecto_deberiaActualizarProyectoCorrectamente(){
        // GIVEN
        proyecto = new Proyecto("Proyecto Test", EmailValue.of("owner@test.com"), "Proyecto de prueba");
        CrearActualizarProyectoDto crearActualizarProyectoDto = new CrearActualizarProyectoDto(null, "owner2@test.com", null, "Actualizacion email");
        Proyecto proyectoEsperado = new Proyecto(proyecto.getNombre(), EmailValue.of(crearActualizarProyectoDto.getEmail()), proyecto.getDescripcion());

        // WHEN
        when(actualizarProyectoService.actualizarProyecto(proyecto.getId().getValor(), crearActualizarProyectoDto)).thenReturn(ListarProyectoDto.toDto(proyectoEsperado));
        ListarProyectoDto resultado = proyectoController.actualizarProyecto(proyecto.getId().getValor(), crearActualizarProyectoDto);

        // THEN
        assertEquals(proyectoEsperado.getNombre(), resultado.getNombre());
        assertEquals(proyectoEsperado.getEmail().getValor(), resultado.getEmail());
        assertEquals(proyectoEsperado.getDescripcion(), resultado.getDescripcion());

    }


}