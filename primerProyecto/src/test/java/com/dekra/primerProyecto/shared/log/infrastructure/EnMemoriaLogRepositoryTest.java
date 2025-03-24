package com.dekra.primerProyecto.shared.log.infrastructure;

import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.domain.model.Log;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnMemoriaLogRepositoryTest {

    private EnMemoriaLogRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EnMemoriaLogRepository();
    }

    @Test
    void guardar_deberiaAgregarLogALaLista() {
        // GIVEN
        IDValue idProyecto = IDValue.of();
        IDValue idUsuario = IDValue.of();
        TipoOperacion tipoOperacion = TipoOperacion.MOD_ATRIBUTOS;
        String comentario = "Log de prueba";
        Log log = new Log(idProyecto, idUsuario, tipoOperacion, comentario);

        // WHEN
        repository.guardar(log);
        List<Log> logs = repository.listar();

        // THEN
        assertNotNull(logs, "La lista de logs no debería ser nula");
        assertEquals(1, logs.size(), "La lista debería contener 1 log");
        assertEquals(log, logs.get(0), "El log guardado debe coincidir con el obtenido");
    }

    @Test
    void listar_deberiaRetornarTodosLosLogsGuardados() {
        // GIVEN
        IDValue idProyecto1 = IDValue.of();
        IDValue idUsuario1 = IDValue.of();
        TipoOperacion tipoOperacion1 = TipoOperacion.MOD_ASIGNACIONES;
        String comentario1 = "Primer log";
        Log log1 = new Log(idProyecto1, idUsuario1, tipoOperacion1, comentario1);

        IDValue idProyecto2 = IDValue.of();
        IDValue idUsuario2 = IDValue.of();
        TipoOperacion tipoOperacion2 = TipoOperacion.MOD_ATRIBUTOS;
        String comentario2 = "Segundo log";
        Log log2 = new Log(idProyecto2, idUsuario2, tipoOperacion2, comentario2);

        repository.guardar(log1);
        repository.guardar(log2);

        // WHEN
        List<Log> logs = repository.listar();

        // THEN
        assertNotNull(logs, "La lista de logs no debería ser nula");
        assertEquals(2, logs.size(), "La lista debería contener 2 logs");
        assertTrue(logs.contains(log1), "La lista debe contener el primer log");
        assertTrue(logs.contains(log2), "La lista debe contener el segundo log");
    }
}