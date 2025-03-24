package com.dekra.primerProyecto.rol.application;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.application.service.CrearRolService;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.infrastrucure.repository.EnMemoriaRolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearRolServiceTest {

    @Mock
    private EnMemoriaRolRepository enMemoriaRolRepository;

    @InjectMocks
    private CrearRolService crearRolService;

    @BeforeEach
    void setUp(){
        enMemoriaRolRepository = new EnMemoriaRolRepository();
        crearRolService = new CrearRolService(enMemoriaRolRepository);
    }

    @Test
    public void crearRol_deberiaCrearYRetornarRolDtoConDatosCorrectos(){
        //GIVEN
        RolDto rolDto = new RolDto("Project manager");

        //WHEN
        RolDto resultado = crearRolService.crearRol(rolDto);

        //THEN
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Project manager", resultado.getNombre(), "El nombre debería coincidir con el ingresado");

        // Verificar que se haya guardado en el repositorio
        Rol rolguardado = enMemoriaRolRepository.listar().get(0);
        assertEquals(1, enMemoriaRolRepository.listar().size());
        assertEquals("Project manager", rolguardado.getNombre(), "Debería haberse guardado el nombre correctamente");
    }

}