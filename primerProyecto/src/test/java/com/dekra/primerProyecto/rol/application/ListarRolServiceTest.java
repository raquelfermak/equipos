package com.dekra.primerProyecto.rol.application;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.application.service.ListarRolService;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.infrastrucure.repository.EnMemoriaRolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarRolServiceTest {

    @Mock
    private EnMemoriaRolRepository enMemoriaRolRepository;

    @InjectMocks
    private ListarRolService listarRolService;

    private Rol rol1;
    private Rol rol2;

    @BeforeEach
    void setUp(){
        //Creamos datos de prueba
        rol1 = new Rol("Product Owner");
        rol2 = new Rol("Scrum Master");
    }

    @Test
    void listar_deberiaRetornarListaVaciaSiNoHayRolesEnElRepositorio(){
        // GIVEN / WHEN
        when(enMemoriaRolRepository.listar()).thenReturn(Collections.emptyList());
        List<RolDto> resultado = listarRolService.listar();

        // THEN
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");
        verify(enMemoriaRolRepository, times(1)).listar();
    }

    @Test
    void listar_deberiaRetornarListaDeRolDtoCorrectamente() {
        // GIVEN / WHEN
        when(enMemoriaRolRepository.listar()).thenReturn(Arrays.asList(rol1, rol2));

        List<RolDto> resultado = listarRolService.listar();

        // THEN
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería retornar 2 usuarios");

        // Verificamos contenido
        RolDto dto1 = resultado.get(0);
        RolDto dto2 = resultado.get(1);
        assertEquals(rol1.getNombre(), dto1.getNombre());
        assertEquals(rol2.getNombre(), dto2.getNombre());

        // Verificamos la llamada al mock
        verify(enMemoriaRolRepository, times(1)).listar();
        // Aseguramos que no haya más interacciones con el mock
        verifyNoMoreInteractions(enMemoriaRolRepository);
    }

}