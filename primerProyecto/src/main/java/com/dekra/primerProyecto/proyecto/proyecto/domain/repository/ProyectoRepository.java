package com.dekra.primerProyecto.proyecto.proyecto.domain.repository;

import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;

import java.util.List;

public interface ProyectoRepository {
    void guardar(Proyecto proyecto);
    List<Proyecto> listar();
    Proyecto buscarPorId(String id);
}
