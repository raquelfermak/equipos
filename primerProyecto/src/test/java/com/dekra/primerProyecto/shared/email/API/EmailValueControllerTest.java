package com.dekra.primerProyecto.shared.email.API;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.application.CrearProyectoService;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.application.service.CrearUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class EmailValueControllerTest {

    private EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;
    private EmailValueController emailValueController;

    @Mock
    private CrearProyectoService crearProyectoService;

    @Mock
    private CrearUsuarioService crearUsuarioService;

    @BeforeEach
    void setup() {
        enMemoriaEmailValueRepository = new EnMemoriaEmailValueRepository();
        emailValueController = new EmailValueController(enMemoriaEmailValueRepository);


    }

    @Test
    void listarDominios_deberiaRetornarDominiosUnicosCuandoExistenDatos() {
        // GIVEN / WHEN
        // Configuramos el mock de CrearUsuarioService para que guarde en el repositorio
        doAnswer(invocation -> {
            // Obtener el DTO pasado como argumento y guardar el email en el repositorio
            UsuarioDto usuarioDto = invocation.getArgument(0);
            enMemoriaEmailValueRepository.guardar(EmailValue.of(usuarioDto.getEmail()));
            return null;
        }).when(crearUsuarioService).crearUsuario(any(UsuarioDto.class));

        // Configuramos el mock de CrearProyectoService para que guarde en el repositorio
        doAnswer(invocation -> {
            // Obtener el DTO pasado como argumento y guardar el email en el repositorio
            CrearActualizarProyectoDto proyectoDto = invocation.getArgument(0);
            enMemoriaEmailValueRepository.guardar(EmailValue.of(proyectoDto.getEmail()));
            return null;
        }).when(crearProyectoService).crearProyecto(any(CrearActualizarProyectoDto.class));

        // Agregamos varios emails al repositorio
        crearUsuarioService.crearUsuario(new UsuarioDto("paco", "paco@gmail.com"));
        crearUsuarioService.crearUsuario(new UsuarioDto("sofia", "sofia@gmail.com"));
        crearUsuarioService.crearUsuario(new UsuarioDto("alba", "alba@outlook.com"));
        crearProyectoService.crearProyecto(new CrearActualizarProyectoDto("p1", "carlos@outlook.com", "descripcion", ""));
        crearProyectoService.crearProyecto(new CrearActualizarProyectoDto("p2", "pepe@hotmail.com", "descripcion", ""));

        Set<String> dominios = emailValueController.listarDominios();
        
        // THEN
        assertTrue(dominios.contains("gmail.com"), "El dominio 'gmail.com' debe estar presente.");
        assertTrue(dominios.contains("outlook.com"), "El dominio 'outlook.com' debe estar presente.");
        assertTrue(dominios.contains("hotmail.com"), "El dominio 'hotmail.com' debe estar presente.");
        assertEquals(3, dominios.size(), "Se deben listar únicamente dos dominios únicos.");
    }

    @Test
    void listarDominios_deberiaRetornarConjuntoVacioCuandoNoExistenDatos() {
        // GIVEN / WHEN
        // Si no se han agregado emails, se espera un conjunto vacío
        Set<String> dominios = emailValueController.listarDominios();

        // THEN
        assertEquals(0, dominios.size(), "El conjunto de dominios debe estar vacío.");
    }
    
}