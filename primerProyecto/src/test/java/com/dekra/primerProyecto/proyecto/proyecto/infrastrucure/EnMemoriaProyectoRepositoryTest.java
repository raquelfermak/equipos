package com.dekra.primerProyecto.proyecto.proyecto.infrastrucure;

import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnMemoriaProyectoRepositoryTest {

    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @BeforeEach
    void setUp() {
        enMemoriaProyectoRepository = new EnMemoriaProyectoRepository();
    }

    @Test
    void guardar_deberiaGuardarProyectoValido() {
        // GIVEN
        EmailValue emailValue = EmailValue.of("juan@outlook.com");
        Proyecto proyecto = new Proyecto("P1", emailValue, "descripcion");

        // WHEN
        enMemoriaProyectoRepository.guardar(proyecto);

        // THEN
        assertEquals(1, enMemoriaProyectoRepository.listar().size(), "La lista debe contener un proyecto");
        assertEquals("P1", enMemoriaProyectoRepository.listar().get(0).getNombre(), "El nombre guardado debe ser 'P1'");
    }

    @Test
    void guardar_deberiaLanzarExcepcionCuandoNombreEsNulo() {
        // GIVEN
        Proyecto proyecto = new Proyecto(null, null, null);

        // WHEN y THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaProyectoRepository.guardar(proyecto),
                "Debe lanzar IllegalArgumentException si el nombre es nulo");
    }

    @Test
    void guardar_deberiaLanzarExcepcionCuandoNombreEstaVacio() {
        // GIVEN
        EmailValue emailValue = EmailValue.of("paco@gmail.com");
        Proyecto proyecto = new Proyecto("", emailValue, "descripcion");

        // WHEN y THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaProyectoRepository.guardar(proyecto),
                "Debe lanzar IllegalArgumentException si el nombre está vacío");
    }

    @Test
    void guardar_deberiaLanzarExcepcionSiElNombreYaExiste() {
        // GIVEN
        EmailValue emailValue1 = EmailValue.of("juan1@outlook.com");
        EmailValue emailValue2 = EmailValue.of("juan2@outlook.com");
        Proyecto usuario1 = new Proyecto("P1", emailValue1, "descripcion");
        Proyecto usuario2 = new Proyecto("P1", emailValue2, "descripcion");

        // WHEN
        enMemoriaProyectoRepository.guardar(usuario1);

        // THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaProyectoRepository.guardar(usuario2),
                "Debe lanzar IllegalArgumentException si se intenta guardar un nombre duplicado");
    }

    @Test
    void listar_deberiaRetornarListaDeProyectos() {
        // GIVEN
        EmailValue emailValue1 = EmailValue.of("juan@gmail.com");
        EmailValue emailValue2 = EmailValue.of("maria@gmail.com");
        enMemoriaProyectoRepository.guardar(new Proyecto("P1", emailValue1, "descripcion"));
        enMemoriaProyectoRepository.guardar(new Proyecto("P2", emailValue2, "descripcion"));

        // WHEN
        var lista = enMemoriaProyectoRepository.listar();

        // THEN
        assertEquals(2, lista.size(), "La lista debe contener 2 usuarios");
    }

    @Test
    void buscarPorId_deberiaRetornarProyectoCorrecto() {
        // GIVEN
        EmailValue emailValue1 = EmailValue.of("juan@gmail.com");
        EmailValue emailValue2 = EmailValue.of("maria@gmail.com");
        Proyecto usuario1 = new Proyecto("P1", emailValue1, "descripcion");
        Proyecto usuario2 = new Proyecto("P2", emailValue2, "descripcion");
        enMemoriaProyectoRepository.guardar(usuario1);
        enMemoriaProyectoRepository.guardar(usuario2);

        String id = enMemoriaProyectoRepository.listar().get(1).getId().getValor();

        // WHEN
        Proyecto encontrado = enMemoriaProyectoRepository.buscarPorId(id);

        // THEN
        assertNotNull(encontrado, "El proyecto no debe ser nulo");
        assertEquals("P2", encontrado.getNombre(), "Debe retornar el proyecto con nombre 'P2'");
    }

    @Test
    void buscarPorId_deberiaRetornarNullSiNoExiste() {
        // GIVEN
        EmailValue emailValue = EmailValue.of("juan@gmail.com");
        enMemoriaProyectoRepository.guardar(new Proyecto("P1", emailValue, "descripcion"));

        // WHEN
        Proyecto noEncontrado = enMemoriaProyectoRepository.buscarPorId("2");

        // THEN
        assertNull(noEncontrado, "Si no existe el proyecto, debe retornar null");
    }
}