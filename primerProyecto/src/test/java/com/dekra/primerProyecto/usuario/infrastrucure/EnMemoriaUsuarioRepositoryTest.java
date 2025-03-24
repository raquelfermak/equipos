package com.dekra.primerProyecto.usuario.infrastrucure;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnMemoriaUsuarioRepositoryTest {
    private EnMemoriaUsuarioRepository enMemoriaUsuarioRepository;

    @BeforeEach
    void setUp() {
        enMemoriaUsuarioRepository = new EnMemoriaUsuarioRepository();
    }

    @Test
    void guardar_deberiaGuardarUsuarioValido() {
        // GIVEN
        EmailValue emailValue = EmailValue.of("juan@outlook.com");
        Usuario usuario = new Usuario("Juan", emailValue);

        // WHEN
        enMemoriaUsuarioRepository.guardar(usuario);

        // THEN
        assertEquals(1, enMemoriaUsuarioRepository.listar().size(), "La lista debe contener un usuario");
        assertEquals("Juan", enMemoriaUsuarioRepository.listar().get(0).getNombre(), "El nombre guardado debe ser 'Juan'");
    }

    @Test
    void guardar_deberiaLanzarExcepcionWHENNombreEsNulo() {
        // GIVEN
        Usuario usuario = new Usuario(null, null);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaUsuarioRepository.guardar(usuario),
                "Debe lanzar IllegalArgumentException si el nombre es nulo");
    }

    @Test
    void guardar_deberiaLanzarExcepcionWHENNombreEstaVacio() {
        // GIVEN
        EmailValue emailValue = EmailValue.of("paco@gmail.com");
        Usuario usuario = new Usuario("", emailValue); // cadena en blanco

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaUsuarioRepository.guardar(usuario),
                "Debe lanzar IllegalArgumentException si el nombre está vacío");
    }

    @Test
    void guardar_deberiaLanzarExcepcionSiElNombreYaExiste() {
        // GIVEN
        EmailValue emailValue1 = EmailValue.of("juan1@outlook.com");
        EmailValue emailValue2 = EmailValue.of("juan2@outlook.com");
        Usuario usuario1 = new Usuario("Juan", emailValue1);
        Usuario usuario2 = new Usuario("Juan", emailValue2);

        // WHEN
        enMemoriaUsuarioRepository.guardar(usuario1);

        // THEN
        assertThrows(IllegalArgumentException.class, () -> enMemoriaUsuarioRepository.guardar(usuario2),
                "Debe lanzar IllegalArgumentException si se intenta guardar un nombre duplicado");
    }

    @Test
    void listar_deberiaRetornarListaDeUsuarios() {
        // GIVEN
        EmailValue emailValue1 = EmailValue.of("juan@gmail.com");
        EmailValue emailValue2 = EmailValue.of("maria@gmail.com");
        enMemoriaUsuarioRepository.guardar(new Usuario("Juan", emailValue1));
        enMemoriaUsuarioRepository.guardar(new Usuario("Maria", emailValue2));

        // WHEN
        var lista = enMemoriaUsuarioRepository.listar();

        // THEN
        assertEquals(2, lista.size(), "La lista debe contener 2 usuarios");
    }

    @Test
    void buscarPorId_deberiaRetornarUsuarioCorrecto() {
        // GIVEN
        EmailValue emailValue1 = EmailValue.of("juan@gmail.com");
        EmailValue emailValue2 = EmailValue.of("maria@gmail.com");
        Usuario usuario1 = new Usuario("Juan", emailValue1);
        Usuario usuario2 = new Usuario("Maria", emailValue2);
        enMemoriaUsuarioRepository.guardar(usuario1);
        enMemoriaUsuarioRepository.guardar(usuario2);

        String id = enMemoriaUsuarioRepository.listar().get(1).getId().getValor();

        // WHEN
        Usuario encontrado = enMemoriaUsuarioRepository.buscarPorId(id);

        // THEN
        assertNotNull(encontrado, "El usuario no debe ser nulo");
        assertEquals("Maria", encontrado.getNombre(), "Debe retornar el usuario con nombre 'Maria'");
    }

    @Test
    void buscarPorId_deberiaRetornarNullSiNoExiste() {
        // GIVEN
        EmailValue emailValue = EmailValue.of("juan@gmail.com");
        enMemoriaUsuarioRepository.guardar(new Usuario("Juan", emailValue));

        // WHEN
        Usuario noEncontrado = enMemoriaUsuarioRepository.buscarPorId("2");

        // THEN
        assertNull(noEncontrado, "Si no existe el usuario, debe retornar null");
    }
}