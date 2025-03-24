package com.dekra.primerProyecto.proyecto.proyectoSnapshot.application;

import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.infrastructure.EnMemoriaProyectoSnapshotRepository;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.version.VersionValue;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CrearProyectoSnapshotService {

    private final EnMemoriaProyectoSnapshotRepository enMemoriaProyectoSnapshotRepository;

    public CrearProyectoSnapshotService(EnMemoriaProyectoSnapshotRepository enMemoriaProyectoSnapshotRepository) {
        this.enMemoriaProyectoSnapshotRepository = enMemoriaProyectoSnapshotRepository;
    }

    public void crearProyectoSnapshot(Proyecto proyecto){

        ProyectoSnapshot ultimoProyectoSnapshot = enMemoriaProyectoSnapshotRepository.buscarUltimoPorId(proyecto.getId().getValor());
        int version = ultimoProyectoSnapshot != null? ultimoProyectoSnapshot.getVersionValue().getNextVersion() : 0;
        Map<IDValue, Set<IDValue>> asignacionesCopia = null;
        if (proyecto.getAsignaciones() != null) {
            asignacionesCopia = proyecto.getAsignaciones().entrySet()
                    .stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> new HashSet<>(entry.getValue()) ));
        }

        ProyectoSnapshot proyectoSnapshot = new ProyectoSnapshot( proyecto.getId(),
                proyecto.getNombre(), proyecto.getEmail(), proyecto.getDescripcion(),
                asignacionesCopia, VersionValue.of(version));

        enMemoriaProyectoSnapshotRepository.guardar(proyectoSnapshot);
    }
}
