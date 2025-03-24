package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarProyectoService {


    private final EnMemoriaProyectoRepository enMemoriaProyectoRepository;

    public ListarProyectoService(EnMemoriaProyectoRepository enMemoriaProyectoRepository) {
        this.enMemoriaProyectoRepository = enMemoriaProyectoRepository;
    }

    public List<ListarProyectoDto> listar(){
        return enMemoriaProyectoRepository.listar()
                .stream()
                .map(ListarProyectoDto::toDto) //mapea Proyecto a ListarProyectoDto usando ListarProyectoDto.toDto(proyecto)
                .collect(Collectors.toList());
    }

}
