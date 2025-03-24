package com.dekra.primerProyecto.shared.log.domain.model;

import com.dekra.primerProyecto.shared.id.IDValue;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {
    @Test
    public void crearLog_deberiaCrearLogConDatosCorrectos() {
        // GIVEN
        IDValue idProyecto = IDValue.of();
        IDValue idUsuario = IDValue.of();
        TipoOperacion tipoOperacion = TipoOperacion.MOD_ATRIBUTOS;
        String comentario = "Comentario de prueba";

        // Capturamos la fecha y hora actuales para una verificación aproximada
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        // WHEN
        Log log = new Log(idProyecto, idUsuario, tipoOperacion, comentario);

        // THEN
        assertNotNull(log.getId(), "Se esperaba que el id se inicializara automáticamente");
        assertEquals(idProyecto, log.getIdProyecto(), "El idProyecto no coincide con el valor esperado");
        assertEquals(idUsuario, log.getIdUsuario(), "El idUsuario no coincide con el valor esperado");
        assertEquals(fechaActual, log.getFechaModificacion(), "La fecha de modificación debe ser la fecha actual");
        assertFalse(log.getHoraModificacion().isBefore(horaActual), "La hora de modificación debe ser igual o posterior a la hora actual");
        assertEquals(tipoOperacion, log.getTipoOperacion(), "El tipo de operación no coincide con el valor esperado");
        assertEquals(comentario, log.getComentario(), "El comentario no coincide con el valor esperado");
    }
}