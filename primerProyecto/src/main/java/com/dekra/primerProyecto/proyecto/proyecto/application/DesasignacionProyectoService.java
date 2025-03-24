package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.AsignacionDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.CrearProyectoSnapshotService;
import com.dekra.primerProyecto.rol.application.event.RolConsultaEvent;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.infrastrucure.repository.EnMemoriaRolRepository;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.application.CrearLogService;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.dekra.primerProyecto.usuario.application.event.UsuarioConsultaEvent;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.infrastrucure.repository.EnMemoriaUsuarioRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class DesasignacionProyectoService {

    private final EnMemoriaProyectoRepository enMemoriaProyectoRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CrearProyectoSnapshotService crearProyectoSnapshotService;
    private final CrearLogService crearLogService;

    public DesasignacionProyectoService(EnMemoriaProyectoRepository enMemoriaProyectoRepository, ApplicationEventPublisher applicationEventPublisher, CrearProyectoSnapshotService crearProyectoSnapshotService, CrearLogService crearLogService) {
        this.enMemoriaProyectoRepository = enMemoriaProyectoRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.crearProyectoSnapshotService = crearProyectoSnapshotService;
        this.crearLogService = crearLogService;
    }

    public ListarProyectoDto desasignarUsuarioARol(AsignacionDto asignacionDto){
        //almaceno en una variable los objetos a los que hacen referencia los id del dto
        Proyecto proyecto = enMemoriaProyectoRepository.buscarPorId(asignacionDto.getProyectoId());

        //si alguno de ellos es nulo, no existe en nuestra aplicacion y lanza excepcion
        if(proyecto ==  null){
            throw new IllegalArgumentException("Algún elemento no existe.");
        }

        // Publicamos un evento para solicitar el Usuario
        UsuarioConsultaEvent usuarioEvent = new UsuarioConsultaEvent(asignacionDto.getUsuarioId());
        applicationEventPublisher.publishEvent(usuarioEvent);
        boolean usuarioExiste = usuarioEvent.isExiste();

        // Publicamos un evento para solicitar el Rol
        RolConsultaEvent rolEvent = new RolConsultaEvent(asignacionDto.getRolId());
        applicationEventPublisher.publishEvent(rolEvent);
        boolean rolExiste = rolEvent.isExiste();

        // Validamos que ambos existan
        if (!usuarioExiste || !rolExiste) {
            throw new IllegalArgumentException("Algún elemento no existe.");
        }

        // Obtener el Map de asignaciones
        Map<IDValue, Set<IDValue>> asignaciones = proyecto.getAsignaciones();
        if (asignaciones == null) {
            throw new IllegalArgumentException("No hay asignaciones");
        }

        // Si el rol no está presente, se crea un Set y se añade el usuario
        if (!asignaciones.containsKey(IDValue.of(rolEvent.getRolId()))) {
            throw new IllegalArgumentException("No existe el rol en el proyecto");
        }else if (!asignaciones.get(IDValue.of(rolEvent.getRolId())).contains(IDValue.of(usuarioEvent.getUsuarioId()))) {
            throw new IllegalArgumentException("No existe el usuario en el proyecto");
        } else {
            // Si  existe, se elimina el usuario del Set correspondiente
            crearProyectoSnapshotService.crearProyectoSnapshot(proyecto);

            Set<IDValue> usuariosSet = asignaciones.get(IDValue.of(rolEvent.getRolId()));
            usuariosSet.remove(IDValue.of(usuarioEvent.getUsuarioId()));
            if(usuariosSet.isEmpty()){
                asignaciones.remove(IDValue.of(rolEvent.getRolId()));
            }
        }

        // Se guarda el proyecto actualizado
        enMemoriaProyectoRepository.guardar(proyecto);

        // Creamos el log
        crearLogService.crearLog(proyecto.getId(), IDValue.of(usuarioEvent.getUsuarioId()), TipoOperacion.MOD_ASIGNACIONES, asignacionDto.getComentario());

        return ListarProyectoDto.toDto(proyecto);
    }

}
