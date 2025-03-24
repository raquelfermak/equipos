package com.dekra.primerProyecto.shared.log.application;

import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.domain.model.Log;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.dekra.primerProyecto.shared.log.infrastructure.EnMemoriaLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CrearLogServiceTest {

    @Mock
    private EnMemoriaLogRepository enMemoriaLogRepository;

    @InjectMocks
    private CrearLogService crearLogService;

    @Test
    void crearLog_deberiaGuardarLogCorrectamente() {
        // GIVEN
        IDValue idProyecto = IDValue.of();
        IDValue idUsuario = IDValue.of();
        TipoOperacion tipoOperacion = TipoOperacion.MOD_ATRIBUTOS;
        String comentario = "Comentario de prueba";

        // WHEN
        crearLogService.crearLog(idProyecto, idUsuario, tipoOperacion, comentario);

        // THEN
        ArgumentCaptor<Log> logCaptor = ArgumentCaptor.forClass(Log.class);
        verify(enMemoriaLogRepository, times(1)).guardar(logCaptor.capture());
        Log logGuardado = logCaptor.getValue();

        assertNotNull(logGuardado, "El log no debería ser nulo");
        assertEquals(idProyecto, logGuardado.getIdProyecto(), "El id del proyecto debe coincidir");
        assertEquals(idUsuario, logGuardado.getIdUsuario(), "El id del usuario debe coincidir");
        assertEquals(tipoOperacion, logGuardado.getTipoOperacion(), "El tipo de operación debe coincidir");
        assertEquals(comentario, logGuardado.getComentario(), "El comentario debe coincidir");
    }
}