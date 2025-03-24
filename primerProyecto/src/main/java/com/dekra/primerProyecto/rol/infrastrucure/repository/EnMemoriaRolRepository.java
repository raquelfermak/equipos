package com.dekra.primerProyecto.rol.infrastrucure.repository;

import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.domain.repository.RolRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnMemoriaRolRepository implements RolRepository {
    private List<Rol> roles = new ArrayList<>();

    @Override
    public void guardar(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        // Verificamos si ya existe un usuario con el mismo nombre
        Rol rolConMismoNombre = roles.stream()
                .filter(u -> u.getNombre().equals(rol.getNombre()))
                .findFirst()
                .orElse(null);

        if (rolConMismoNombre != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre.");
        }
        roles.add(rol);
    }

    @Override
    public List<Rol> listar() {
        return roles;
    }

    @Override
    public Rol buscarPorId(String id) {
        return roles.stream()
                .filter(rol -> rol.getId().getValor().equals(id))
                .findFirst()
                .orElse(null);
    }
}
