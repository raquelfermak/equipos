package com.dekra.primerProyecto.usuario.application.service;

import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarUsuarioService {

    private final EnMemoriaUsuarioRepository enMemoriaUsuarioRepository;

    public ListarUsuarioService(EnMemoriaUsuarioRepository enMemoriaUsuarioRepository) {
        this.enMemoriaUsuarioRepository = enMemoriaUsuarioRepository;
    }

    public List<UsuarioDto> listar(){
        return enMemoriaUsuarioRepository.listar()
                .stream()
                .map(UsuarioDto::toDto) //mapea Usuario a UsuarioDto usando UsuarioDto.toDto(usuario)
                .collect(Collectors.toList());
    }
}
