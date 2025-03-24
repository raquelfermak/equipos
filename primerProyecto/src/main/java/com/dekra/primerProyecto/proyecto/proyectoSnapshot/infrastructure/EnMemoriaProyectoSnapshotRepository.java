package com.dekra.primerProyecto.proyecto.proyectoSnapshot.infrastructure;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.repository.ProyectoSnapshotRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class EnMemoriaProyectoSnapshotRepository implements ProyectoSnapshotRepository {

    private final List<ProyectoSnapshot> proyectoSnapshots = new ArrayList<>();

    @Override
    public void guardar(ProyectoSnapshot proyectoSnapshot) {
        //anadimos el nuevo fotograma a la lista
        proyectoSnapshots.add(proyectoSnapshot);
    }

    @Override
    public List<ProyectoSnapshot> listar() {
        return proyectoSnapshots;
    }

    @Override
    public ProyectoSnapshot buscarUltimoPorId(String id) {
        return proyectoSnapshots.stream()
                .filter(proyectoSnapshot -> proyectoSnapshot.getId().getValor().equals(id))
                        .max(Comparator.comparing(ProyectoSnapshot::getLocalDateTime))
                        .orElse(null);
    }
}
