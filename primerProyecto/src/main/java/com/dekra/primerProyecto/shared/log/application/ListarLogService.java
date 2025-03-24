package com.dekra.primerProyecto.shared.log.application;

import com.dekra.primerProyecto.shared.log.API.dto.LogDto;
import com.dekra.primerProyecto.shared.log.infrastructure.EnMemoriaLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarLogService {

    private final EnMemoriaLogRepository enMemoriaLogRepository;

    public ListarLogService(EnMemoriaLogRepository enMemoriaLogRepository) {
        this.enMemoriaLogRepository = enMemoriaLogRepository;
    }

    public List<LogDto> listar(){
        return enMemoriaLogRepository.listar().stream()
                .map(LogDto::toDto)
                .collect(Collectors.toList());
    }
}
