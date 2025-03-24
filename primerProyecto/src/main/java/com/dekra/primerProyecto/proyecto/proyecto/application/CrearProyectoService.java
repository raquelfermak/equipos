package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearProyectoService {


    private final EnMemoriaProyectoRepository enMemoriaProyectoRepository;
    private final EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    public CrearProyectoService(EnMemoriaProyectoRepository enMemoriaProyectoRepository, EnMemoriaEmailValueRepository enMemoriaEmailValueRepository) {
        this.enMemoriaProyectoRepository = enMemoriaProyectoRepository;
        this.enMemoriaEmailValueRepository = enMemoriaEmailValueRepository;
    }

    public ListarProyectoDto crearProyecto(CrearActualizarProyectoDto crearActualizarProyectoDto){
        //crea un nuevo objeto de tipo Proyecto con la informacion del dto recibido por parametro
        EmailValue emailValue = EmailValue.of(crearActualizarProyectoDto.getEmail());
        Proyecto nuevoProyecto = new Proyecto(crearActualizarProyectoDto.getNombre(), emailValue, crearActualizarProyectoDto.getDescripcion());

        //llama al repositorio para almacenar el objeto
        enMemoriaProyectoRepository.guardar(nuevoProyecto);
        enMemoriaEmailValueRepository.guardar(emailValue);

        //devuelve el nuevo objeto como instancia de CrearProyectoDto
        return ListarProyectoDto.toDto(nuevoProyecto);
    }
}
