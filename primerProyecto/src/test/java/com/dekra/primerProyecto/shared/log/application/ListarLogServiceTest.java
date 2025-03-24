package com.dekra.primerProyecto.shared.log.application;

import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.API.dto.LogDto;
import com.dekra.primerProyecto.shared.log.domain.model.Log;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.dekra.primerProyecto.shared.log.infrastructure.EnMemoriaLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarLogServiceTest {
    // Dependencia mockeada
    private EnMemoriaLogRepository enMemoriaLogRepository;

    // Clase a probar
    private ListarLogService listarLogService;

    @BeforeEach
    void setUp() {
        enMemoriaLogRepository = new EnMemoriaLogRepository();
        listarLogService = new ListarLogService(enMemoriaLogRepository);
    }

    @Test
    void listar_deberiaRetornarListaVaciaSiNoHayLogs() {
        // WHEN
        var resultado = listarLogService.listar();

        // THEN
        assertNotNull(resultado, "La lista devuelta no debe ser nula");
        assertTrue(resultado.isEmpty(), "La lista devuelta debe estar vacía si el repositorio está vacío");
    }

    @Test
    void listar_deberiaRetornarListaConLogsSiRepositorioNoEstaVacio() {
        // GIVEN: Se tienen logs registrados en el repositorio
        Log log1 = new Log(IDValue.of(), IDValue.of(), TipoOperacion.MOD_ATRIBUTOS, "Primer comentario");
        Log log2 = new Log(IDValue.of(), IDValue.of(), TipoOperacion.MOD_ASIGNACIONES, "Segundo comentario");
        enMemoriaLogRepository.guardar(log1);
        enMemoriaLogRepository.guardar(log2);

        // WHEN: Se invoca el método listar() del servicio
        List<LogDto> listaLogs = listarLogService.listar();

        // THEN: Se debe retornar una lista no nula con dos elementos y cada elemento debe ser la conversión a LogDto
        assertNotNull(listaLogs, "La lista retornada no debe ser nula");
        assertEquals(2, listaLogs.size(), "La lista debe contener dos elementos");
    }

}