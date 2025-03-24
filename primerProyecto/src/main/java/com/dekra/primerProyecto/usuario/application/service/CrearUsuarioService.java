package com.dekra.primerProyecto.usuario.application.service;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearUsuarioService {

    private final EnMemoriaUsuarioRepository enMemoriaUsuarioRepository;
    private final EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    public CrearUsuarioService(EnMemoriaUsuarioRepository enMemoriaUsuarioRepository, EnMemoriaEmailValueRepository enMemoriaEmailValueRepository) {
        this.enMemoriaUsuarioRepository = enMemoriaUsuarioRepository;
        this.enMemoriaEmailValueRepository = enMemoriaEmailValueRepository;
    }

    public UsuarioDto crearUsuario(UsuarioDto usuarioDto){
        //crea un nuevo objeto de tipo Usuario con la informacion del dto recibido por parametro
        EmailValue emailValue = EmailValue.of(usuarioDto.getEmail());
        Usuario nuevoUsuario = new Usuario(usuarioDto.getNombre(),emailValue);

        //llama al repositorio para almacenar el objeto
        enMemoriaUsuarioRepository.guardar(nuevoUsuario);
        enMemoriaEmailValueRepository.guardar(emailValue);

        //devuelve el nuevo objeto como instancia de UsuarioDto
        return UsuarioDto.toDto(nuevoUsuario);
    }
}
