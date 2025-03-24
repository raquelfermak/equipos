package com.dekra.primerProyecto.shared.log.application;

import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.domain.model.Log;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.dekra.primerProyecto.shared.log.infrastructure.EnMemoriaLogRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearLogService {

    private final EnMemoriaLogRepository enMemoriaLogRepository;

    public CrearLogService(EnMemoriaLogRepository enMemoriaLogRepository) {
        this.enMemoriaLogRepository = enMemoriaLogRepository;
    }

    public void crearLog(IDValue idProyecto, IDValue idUsuario, TipoOperacion tipoOperacion, String comentario){
        Log log = new Log(idProyecto, idUsuario, tipoOperacion, comentario);
        enMemoriaLogRepository.guardar(log);
    }
}
