package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.application.CrearProyectoService;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearProyectoServiceTest {

    private CrearProyectoService crearProyectoService;
    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @BeforeEach
    void setUp() {
        enMemoriaProyectoRepository = new EnMemoriaProyectoRepository();
        EnMemoriaEmailValueRepository enMemoriaEmailValueRepository = new EnMemoriaEmailValueRepository();
        crearProyectoService = new CrearProyectoService(enMemoriaProyectoRepository, enMemoriaEmailValueRepository);
    }

    @Test
    public void crearProyecto_deberiaCrearYRetornarProyectoDtoConDatosCorrectos(){
        // GIVEN
        CrearActualizarProyectoDto crearActualizarProyectoDto = new CrearActualizarProyectoDto("Proyecto1", "carlos@example.com", "primer proyecto", "");

        // WHEN
        ListarProyectoDto resultado = crearProyectoService.crearProyecto(crearActualizarProyectoDto);

        // THEN
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Proyecto1", resultado.getNombre(), "El nombre debería coincidir con el ingresado");
        assertEquals("carlos@example.com", resultado.getEmail(), "El email debería coincidir con el ingresado");
        assertEquals("primer proyecto", resultado.getDescripcion(), "La descripcion deberia coincidir con el ingresado");

        // Verificar que se haya guardado en el repositorio
        Proyecto proyectoGuardado = enMemoriaProyectoRepository.listar().get(0);
        assertEquals(1, enMemoriaProyectoRepository.listar().size());
        assertEquals("Proyecto1", proyectoGuardado.getNombre(), "Debería haberse guardado el nombre correctamente");
        assertEquals("carlos@example.com", proyectoGuardado.getEmail().getValor(), "Debería haberse guardado el email correctamente");
        assertEquals("primer proyecto", proyectoGuardado.getDescripcion(), "Debería haberse guardado la descripcion correctamente");
    }

    @Test
    void crearProyecto_deberiaLanzarExcepcionSiNombreEsNulo() {
        // GIVEN
        CrearActualizarProyectoDto crearActualizarProyectoDto = new CrearActualizarProyectoDto( );
        crearActualizarProyectoDto.setNombre(null);
        crearActualizarProyectoDto.setEmail("nulo@example.com");
        crearActualizarProyectoDto.setDescripcion("descripcion");

        // WHEN / THEN
        // El repositorio lanza IllegalArgumentException si el nombre es nulo
        assertThrows(IllegalArgumentException.class, () -> crearProyectoService.crearProyecto(crearActualizarProyectoDto),
                "Debería lanzar IllegalArgumentException por nombre nulo");
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiNombreEstaVacio() {
        // GIVEN
        CrearActualizarProyectoDto crearActualizarProyectoDto = new CrearActualizarProyectoDto("", "vacio@example.com", "descripcion", "");

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> crearProyectoService.crearProyecto(crearActualizarProyectoDto),
                "Debería lanzar IllegalArgumentException por nombre vacío");
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiNombreYaExiste() {
        // GIVEN
        CrearActualizarProyectoDto crearActualizarProyectoDto1 = new CrearActualizarProyectoDto("Proyecto", "carlos@example.com", "descripcion", "");
        CrearActualizarProyectoDto crearActualizarProyectoDto2 = new CrearActualizarProyectoDto("Proyecto", "carlos2@example.com", "descripcion", "");

        // Guardar el primer usuario
        crearProyectoService.crearProyecto(crearActualizarProyectoDto1);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> crearProyectoService.crearProyecto(crearActualizarProyectoDto2),
                "Debería lanzar IllegalArgumentException por nombre duplicado");
    }

}