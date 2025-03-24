package com.dekra.primerProyecto.proyecto.proyectoSnapshot.application;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.dto.ListarProyectoSnapshotDto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.infrastructure.EnMemoriaProyectoSnapshotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarProyectoSnapshotService {


    private final EnMemoriaProyectoSnapshotRepository enMemoriaProyectoSnapshotRepository;

    public ListarProyectoSnapshotService(EnMemoriaProyectoSnapshotRepository enMemoriaProyectoSnapshotRepository) {
        this.enMemoriaProyectoSnapshotRepository = enMemoriaProyectoSnapshotRepository;
    }

    public List<ListarProyectoSnapshotDto> listar(){
        return enMemoriaProyectoSnapshotRepository.listar()
                .stream()
                .map(ListarProyectoSnapshotDto::toDto) //mapea ProyectoSnapshot a ListarProyectoSnapshotDto usando ListarProyectoSnapshotDto.toDto(proyectoSnapshot)
                .collect(Collectors.toList());
    }

}
