package com.dekra.primerProyecto.rol.API.controller;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.application.service.CrearRolService;
import com.dekra.primerProyecto.rol.application.service.ListarRolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolControllerTest {
    
    @Mock
    private CrearRolService crearRolService;
    
    @Mock
    private ListarRolService listarRolService;
    
    @InjectMocks
    private RolController rolController;
    
    @Test
    void crearRol_deberiaRetornarRolDtoCreado(){
        // GIVEN
        RolDto rolDtoEntrada = new RolDto("Product Owner");
        RolDto rolDtoSalida = new RolDto("Scrum Master");


        // WHEN
        when(crearRolService.crearRol(rolDtoEntrada)).thenReturn(rolDtoSalida);
        RolDto resultado = rolController.crearRol(rolDtoEntrada);

        // THEN
        assertEquals(rolDtoSalida, resultado,
                "El controlador debería retornar el RolDto que regresa el servicio de creación");
        verify(crearRolService, times(1)).crearRol(rolDtoEntrada);
    }

    @Test
    void listarUsuarios_deberiaRetornarListaDeRoles() {
        // GIVEN
        RolDto usuario1 =new RolDto("Product Owner");
        RolDto usuario2 =new RolDto("Scrum Master");
        List<RolDto> listaEsperada = Arrays.asList(usuario1, usuario2);


        // WHEN
        when(listarRolService.listar()).thenReturn(listaEsperada);
        List<RolDto> resultado = rolController.listarRoles();

        // THEN
        assertEquals(listaEsperada, resultado,
                "El controlador debería retornar la lista que devuelve el servicio de listado");
        verify(listarRolService, times(1)).listar();
    }

}