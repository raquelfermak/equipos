package com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.repository;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;

import java.util.List;

public interface ProyectoSnapshotRepository {
    void guardar(ProyectoSnapshot proyectoSnapshot);
    List<ProyectoSnapshot> listar();
    ProyectoSnapshot buscarUltimoPorId(String id);
}
