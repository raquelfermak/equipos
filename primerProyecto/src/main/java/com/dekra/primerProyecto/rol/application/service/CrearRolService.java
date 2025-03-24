package com.dekra.primerProyecto.rol.application.service;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.infrastrucure.repository.EnMemoriaRolRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearRolService {

    private final EnMemoriaRolRepository enMemoriaRolRepository;

    public CrearRolService(EnMemoriaRolRepository enMemoriaRolRepository) {
        this.enMemoriaRolRepository = enMemoriaRolRepository;
    }

    public RolDto crearRol(RolDto rolDto){
        //crea un nuevo objeto de tipo Rol con la informacion del dto recibido por parametro
        Rol nuevoRol = new Rol(rolDto.getNombre());

        //llama al repositorio para almacenar el objeto
        enMemoriaRolRepository.guardar(nuevoRol);

        //devuelve el nuevo objeto como instancia de RolDto
        return RolDto.toDto(nuevoRol);
    }
}
