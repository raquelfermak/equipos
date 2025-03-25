package com.dekra.primerProyecto.rol.infrastrucure.repository;


import com.dekra.primerProyecto.rol.domain.model.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnMemoriaRolRepositoryTest {

    private EnMemoriaRolRepository enMemoriaRolRepository;

    @BeforeEach
    void setUp() {
        enMemoriaRolRepository = new EnMemoriaRolRepository();
    }

    @Test
    void guardar_deberiaGuardarRolValido() {
        // GIVEN
        Rol rol = new Rol("Developer");

        // WHEN
        enMemoriaRolRepository.guardar(rol);

        // THEN
        assertEquals(1, enMemoriaRolRepository.listar().size(), "La lista debe contener un rol");
        assertEquals("Developer", enMemoriaRolRepository.listar().get(0).getNombre(), "El nombre guardado debe ser 'Developer'");
    }

    @Test
    void guardar_deberiaLanzarExcepcionCuandoNombreEsNulo() {
        // GIVEN
        Rol rol = new Rol(null);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaRolRepository.guardar(rol),
                "Debe lanzar IllegalArgumentException si el nombre es nulo");
    }

    @Test
    void guardar_deberiaLanzarExcepcionCuandoNombreEstaVacio() {
        // GIVEN
        Rol rol = new Rol("");

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaRolRepository.guardar(rol),
                "Debe lanzar IllegalArgumentException si el nombre está vacío");
    }

    @Test
    void guardar_deberiaLanzarExcepcionSiElNombreYaExiste() {
        // GIVEN
        Rol rol1 = new Rol("Developer");
        Rol rol2 = new Rol("Developer");

        // WHEN
        enMemoriaRolRepository.guardar(rol1);

        // THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaRolRepository.guardar(rol2),
                "Debe lanzar IllegalArgumentException si se intenta guardar un nombre duplicado");
    }

    @Test
    void listar_deberiaRetornarListaDeRoles() {
        // GIVEN
        enMemoriaRolRepository.guardar(new Rol("Developer"));
        enMemoriaRolRepository.guardar(new Rol("Product owner"));

        // WHEN
        var lista = enMemoriaRolRepository.listar();

        // THEN
        assertEquals(2, lista.size(), "La lista debe contener 2 roles");
    }

    @Test
    void buscarPorId_deberiaRetornarRolCorrecto() {
        // GIVEN
        Rol rol1 = new Rol("Product owner");
        Rol rol2 = new Rol("Developer");
        enMemoriaRolRepository.guardar(rol1);
        enMemoriaRolRepository.guardar(rol2);

        String id = enMemoriaRolRepository.listar().get(1).getId().getValor();

        // WHEN
        Rol encontrado = enMemoriaRolRepository.buscarPorId(id);

        // THEN
        assertNotNull(encontrado, "El rol no debe ser nulo");
        assertEquals("Developer", encontrado.getNombre(), "Debe retornar el rol con nombre 'Developer'");
    }

    @Test
    void buscarPorId_deberiaRetornarNullSiNoExiste() {
        // GIVEN
        enMemoriaRolRepository.guardar(new Rol("Developer"));

        // WHEN
        Rol noEncontrado = enMemoriaRolRepository.buscarPorId("2");

        // THEN
        assertNull(noEncontrado, "Si no existe el rol, debe retornar null");
    }

}