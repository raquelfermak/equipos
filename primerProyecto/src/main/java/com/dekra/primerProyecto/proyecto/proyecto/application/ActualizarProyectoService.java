package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.CrearProyectoSnapshotService;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import com.dekra.primerProyecto.shared.log.application.CrearLogService;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import org.springframework.stereotype.Service;

@Service
public class ActualizarProyectoService {
    
    private final EnMemoriaProyectoRepository enMemoriaProyectoRepository;
    private final CrearProyectoSnapshotService crearProyectoSnapshotService;
    private final CrearLogService crearLogService;
    private final EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    public ActualizarProyectoService(EnMemoriaProyectoRepository enMemoriaProyectoRepository, CrearProyectoSnapshotService crearProyectoSnapshotService, CrearLogService crearLogService, EnMemoriaEmailValueRepository enMemoriaEmailValueRepository) {
        this.enMemoriaProyectoRepository = enMemoriaProyectoRepository;
        this.crearProyectoSnapshotService = crearProyectoSnapshotService;
        this.crearLogService = crearLogService;
        this.enMemoriaEmailValueRepository = enMemoriaEmailValueRepository;
    }


    public ListarProyectoDto actualizarProyecto(String id, CrearActualizarProyectoDto crearActualizarProyectoDto){
        Proyecto proyecto = enMemoriaProyectoRepository.buscarPorId(id);

        if(proyecto != null){
            crearProyectoSnapshotService.crearProyectoSnapshot(proyecto);
            proyecto.setNombre(crearActualizarProyectoDto.getNombre() != null? crearActualizarProyectoDto.getNombre() : proyecto.getNombre());
            proyecto.setEmail(crearActualizarProyectoDto.getEmail() != null? EmailValue.of(crearActualizarProyectoDto.getEmail()) : proyecto.getEmail());
            proyecto.setDescripcion(crearActualizarProyectoDto.getDescripcion() != null? crearActualizarProyectoDto.getDescripcion() : proyecto.getDescripcion());

            enMemoriaProyectoRepository.guardar(proyecto);
            enMemoriaEmailValueRepository.guardar(proyecto.getEmail());

            // Creamos el log
            crearLogService.crearLog(proyecto.getId(), null, TipoOperacion.MOD_ATRIBUTOS, crearActualizarProyectoDto.getComentario());

            return ListarProyectoDto.toDto(proyecto);

        }else{
            throw new IllegalArgumentException("No existe ese proyecto");
        }

    }
}
