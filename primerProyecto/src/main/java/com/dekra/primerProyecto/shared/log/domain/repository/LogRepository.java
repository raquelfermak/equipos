package com.dekra.primerProyecto.shared.log.domain.repository;

import com.dekra.primerProyecto.shared.log.domain.model.Log;

import java.util.List;

public interface LogRepository {
    void guardar(Log log);
    List<Log> listar();
}
