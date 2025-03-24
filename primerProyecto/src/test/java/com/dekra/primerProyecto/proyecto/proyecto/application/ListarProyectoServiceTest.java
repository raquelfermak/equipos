package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.application.ListarProyectoService;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
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
class ListarProyectoServiceTest {

    @Mock
    private EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    @InjectMocks
    private ListarProyectoService listarProyectoService;

    private Proyecto proyecto1;
    private Proyecto proyecto2;

    @BeforeEach
    void setUp() {
        // Creamos datos de prueba
        EmailValue emailValue1 = EmailValue.of("juan@example.com");
        EmailValue emailValue2 = EmailValue.of("maria@example.com");
        proyecto1 = new Proyecto("Proyecto1", emailValue1, "descripcion");
        proyecto2 = new Proyecto("Proyecto2", emailValue2, "descripcion");
    }

    @Test
    void listar_deberiaRetornarListaVaciaSiNoHayProyectosEnElRepositorio() {
        // GIVEN (mock retorna lista vacía)
        when(enMemoriaProyectoRepository.listar()).thenReturn(Collections.emptyList());

        // WHEN
        List<ListarProyectoDto> resultado = listarProyectoService.listar();

        // THEN
        assertNotNull(resultado, "La lista no debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");
        verify(enMemoriaProyectoRepository, times(1)).listar();
    }

    @Test
    void listar_deberiaRetornarListaDeProyectosDtoCorrectamente() {
        // GIVEN (mock retorna lista con dos usuarios)
        when(enMemoriaProyectoRepository.listar()).thenReturn(Arrays.asList(proyecto1, proyecto2));

        // WHEN
        List<ListarProyectoDto> resultado = listarProyectoService.listar();

        // THEN
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Debería retornar 2 proyectos");

        // Verificamos contenido
        ListarProyectoDto dto1 = resultado.get(0);
        ListarProyectoDto dto2 = resultado.get(1);
        assertEquals(proyecto1.getNombre(), dto1.getNombre());
        assertEquals(proyecto1.getEmail().getValor(), dto1.getEmail());
        assertEquals(proyecto1.getDescripcion(), dto1.getDescripcion());
        assertEquals(proyecto2.getNombre(), dto2.getNombre());
        assertEquals(proyecto2.getEmail().getValor(), dto2.getEmail());
        assertEquals(proyecto2.getDescripcion(), dto2.getDescripcion());

        // Verificamos la llamada al mock
        verify(enMemoriaProyectoRepository, times(1)).listar();
        // Aseguramos que no haya más interacciones con el mock
        verifyNoMoreInteractions(enMemoriaProyectoRepository);
    }
}