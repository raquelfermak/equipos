package com.dekra.primerProyecto.rol.application.service;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.infrastrucure.repository.EnMemoriaRolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarRolService {

    private final EnMemoriaRolRepository enMemoriaRolRepository;

    public ListarRolService(EnMemoriaRolRepository enMemoriaRolRepository) {
        this.enMemoriaRolRepository = enMemoriaRolRepository;
    }

    public List<RolDto> listar(){
        return enMemoriaRolRepository.listar() .stream()
                .map(RolDto::toDto) // mapea Rol a RolDto usando RolDto.toDto(rol)
                .collect(Collectors.toList());
    }
}
