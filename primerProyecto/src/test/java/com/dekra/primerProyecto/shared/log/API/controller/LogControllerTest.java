package com.dekra.primerProyecto.shared.log.API.controller;

import com.dekra.primerProyecto.shared.log.API.dto.LogDto;
import com.dekra.primerProyecto.shared.log.application.ListarLogService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

    @Mock
    private ListarLogService listarLogService;

    @InjectMocks
    private LogController logController;

    @Test
    void crearProyectoSnapshot_deberiaCrearSnapshotConAsignaciones() {
        // GIVEN
        List<LogDto> mockedLogs = Collections.singletonList(new LogDto());
        when(listarLogService.listar()).thenReturn(mockedLogs);

        // WHEN
        List<LogDto> result = logController.listarLogs();

        // THEN (
        assertEquals(mockedLogs, result,
                "Se esperaba que la lista devuelta fuera la misma que la del servicio");
        verify(listarLogService).listar();
    }

}