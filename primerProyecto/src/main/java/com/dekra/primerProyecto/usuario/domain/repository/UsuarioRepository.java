package com.dekra.primerProyecto.usuario.domain.repository;

import com.dekra.primerProyecto.usuario.domain.model.Usuario;

import java.util.List;

public interface UsuarioRepository {
    void guardar(Usuario usuario);
    List<Usuario> listar();
    Usuario buscarPorId(String id);
}
