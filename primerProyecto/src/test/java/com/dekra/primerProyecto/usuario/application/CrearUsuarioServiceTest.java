package com.dekra.primerProyecto.usuario.application;

import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.application.service.CrearUsuarioService;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioServiceTest {

    private CrearUsuarioService crearUsuarioService;
    private EnMemoriaUsuarioRepository enMemoriaUsuarioRepository;
    private EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    @BeforeEach
    void setUp() {
        enMemoriaUsuarioRepository = new EnMemoriaUsuarioRepository();
        enMemoriaEmailValueRepository = new EnMemoriaEmailValueRepository();
        crearUsuarioService = new CrearUsuarioService(enMemoriaUsuarioRepository, enMemoriaEmailValueRepository);
    }

    @Test
    public void crearUsuario_deberiaCrearYRetornarUsuarioDtoConDatosCorrectos(){
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto("Carlos", "carlos@example.com");

        // WHEN
        UsuarioDto resultado = crearUsuarioService.crearUsuario(usuarioDto);

        // THEN
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Carlos", resultado.getNombre(), "El nombre debería coincidir con el ingresado");
        assertEquals("carlos@example.com", resultado.getEmail(), "El email debería coincidir con el ingresado");

        // Verificar que se haya guardado en el repositorio
        Usuario usuarioguardado = enMemoriaUsuarioRepository.listar().get(0);
        assertEquals(1, enMemoriaUsuarioRepository.listar().size());
        assertEquals("Carlos", usuarioguardado.getNombre(), "Debería haberse guardado el nombre correctamente");
        assertEquals("carlos@example.com", usuarioguardado.getEmail().getValor(), "Debería haberse guardado el email correctamente");
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiNombreEsNulo() {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto(null, "nulo@example.com");

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> crearUsuarioService.crearUsuario(usuarioDto),
                "Debería lanzar IllegalArgumentException por nombre nulo");
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiNombreEstaVacio() {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto("    ", "vacio@example.com");

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> crearUsuarioService.crearUsuario(usuarioDto),
                "Debería lanzar IllegalArgumentException por nombre vacío");
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionSiNombreYaExiste() {
        // GIVEN
        UsuarioDto usuarioDto1 = new UsuarioDto("Carlos", "carlos@example.com");
        UsuarioDto usuarioDto2 = new UsuarioDto("Carlos", "carlos2@example.com");

        crearUsuarioService.crearUsuario(usuarioDto1);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> crearUsuarioService.crearUsuario(usuarioDto2),
                "Debería lanzar IllegalArgumentException por nombre duplicado");
    }
}