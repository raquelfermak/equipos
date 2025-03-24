package com.dekra.primerProyecto.proyecto.proyecto.infrastrucure;

import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.repository.ProyectoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnMemoriaProyectoRepository implements ProyectoRepository {
    private final List<Proyecto> proyectos = new ArrayList<>();

    @Override
    public void guardar(Proyecto proyecto){
        if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }

        // Buscamos si ya existe un proyecto con el mismo ID
        Proyecto proyectoExistente = buscarPorId(proyecto.getId().getValor());

        if (proyectoExistente != null) {
            // Si ya existe, lo actualizamos:
            proyectoExistente.setNombre(proyecto.getNombre());
            proyectoExistente.setEmail(proyecto.getEmail());
            proyectoExistente.setDescripcion(proyecto.getDescripcion());
            proyectoExistente.setAsignaciones(proyecto.getAsignaciones());
        } else {
            // Si no existe, lo agregamos a la lista
            // Verificamos si ya existe un proyecto con el mismo nombre
            Proyecto proyectoConMismoNombre = proyectos.stream()
                    .filter(p -> p.getNombre().equals(proyecto.getNombre()))
                    .findFirst()
                    .orElse(null);

            if (proyectoConMismoNombre != null) {
                throw new IllegalArgumentException("Ya existe un proyecto con ese nombre.");
            }
            proyectos.add(proyecto);
        }
    }

    @Override
    public List<Proyecto> listar() {
        return proyectos;
    }

    @Override
    public Proyecto buscarPorId(String id) {
        return proyectos.stream()
                .filter(proyecto -> proyecto.getId().getValor().equals(id))
                .findFirst()
                .orElse(null);
    }
}
