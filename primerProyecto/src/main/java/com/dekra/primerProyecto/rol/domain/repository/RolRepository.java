package com.dekra.primerProyecto.rol.domain.repository;


import com.dekra.primerProyecto.rol.domain.model.Rol;

import java.util.List;

public interface RolRepository {
    void guardar(Rol rol);
    List<Rol> listar();
    Rol buscarPorId(String id);
}
