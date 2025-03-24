package com.dekra.primerProyecto.shared.log.infrastructure;

import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.domain.model.Log;
import com.dekra.primerProyecto.shared.log.domain.repository.LogRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnMemoriaLogRepository implements LogRepository {

    List<Log> logs = new ArrayList<>();

    @Override
    public void guardar(Log log) {
        logs.add(log);
    }

    @Override
    public List<Log> listar() {
        return logs;
    }
}
