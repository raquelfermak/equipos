package com.dekra.primerProyecto.proyecto.proyecto.application;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.AsignacionDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.CrearProyectoSnapshotService;
import com.dekra.primerProyecto.rol.application.event.RolConsultaEvent;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.log.application.CrearLogService;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.dekra.primerProyecto.usuario.application.event.UsuarioConsultaEvent;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.proyecto.proyecto.infrastrucure.EnMemoriaProyectoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AsignacionProyectoService {

    private final EnMemoriaProyectoRepository enMemoriaProyectoRepository;
    private final CrearProyectoSnapshotService crearProyectoSnapshotService;
    private final CrearLogService crearLogService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AsignacionProyectoService(EnMemoriaProyectoRepository enMemoriaProyectoRepository, CrearProyectoSnapshotService crearProyectoSnapshotService, CrearLogService crearLogService, ApplicationEventPublisher applicationEventPublisher) {
        this.enMemoriaProyectoRepository = enMemoriaProyectoRepository;
        this.crearProyectoSnapshotService = crearProyectoSnapshotService;
        this.crearLogService = crearLogService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public ListarProyectoDto asignarUsuarioARol(AsignacionDto asignacionDto){
        //almaceno en una variable los objetos a los que hacen referencia los id del dto
        Proyecto proyecto = enMemoriaProyectoRepository.buscarPorId(asignacionDto.getProyectoId());

        //si alguno de ellos es nulo, no existe en nuestra aplicacion y lanza excepcion
        if(proyecto ==  null){
            throw new IllegalArgumentException("Algún elemento no existe.");
        }


        // Publicamos un evento para solicitar el Usuario
        UsuarioConsultaEvent usuarioEvent = new UsuarioConsultaEvent(asignacionDto.getUsuarioId());
        applicationEventPublisher.publishEvent(usuarioEvent);
        Usuario usuario = usuarioEvent.getUsuario();

        // Publicamos un evento para solicitar el Rol
        RolConsultaEvent rolEvent = new RolConsultaEvent(asignacionDto.getRolId());
        applicationEventPublisher.publishEvent(rolEvent);
        Rol rol = rolEvent.getRol();

        // Validamos que ambos existan
        if (usuario == null || rol == null) {
            throw new IllegalArgumentException("Algún elemento no existe.");
        }
        crearProyectoSnapshotService.crearProyectoSnapshot(proyecto);

        // Obtener o inicializar el Map de asignaciones
        Map<Rol, Set<Usuario>> asignaciones = proyecto.getAsignaciones();
        if (asignaciones == null) {
            asignaciones = new HashMap<>();
            proyecto.setAsignaciones(asignaciones);
        }

        // Si el rol no está presente, se crea un Set y se añade el usuario
        if (!asignaciones.containsKey(rol)) {
            Set<Usuario> usuariosSet = new HashSet<>();
            usuariosSet.add(usuario);
            asignaciones.put(rol, usuariosSet);
        } else {
            // Si ya existe, se añade el usuario al Set correspondiente
            Set<Usuario> usuariosSet = asignaciones.get(rol);
            usuariosSet.add(usuario);
        }

        // Se guarda el proyecto actualizado
        enMemoriaProyectoRepository.guardar(proyecto);

        // Creamos el log
        crearLogService.crearLog(proyecto.getId(), usuario.getId(), TipoOperacion.MOD_ASIGNACIONES, asignacionDto.getComentario());

        return ListarProyectoDto.toDto(proyecto);
    }

}
