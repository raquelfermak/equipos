package com.dekra.primerProyecto.usuario.infrastrucure.repository;

import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnMemoriaUsuarioRepository implements UsuarioRepository {
    private List<Usuario> usuarios = new ArrayList<>();


    @Override
    public void guardar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        // Verificamos si ya existe un usuario con el mismo nombre
        Usuario usuarioConMismoNombre = usuarios.stream()
                .filter(u -> u.getNombre().equals(usuario.getNombre()))
                .findFirst()
                .orElse(null);

        if (usuarioConMismoNombre != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre.");
        }
        usuarios.add(usuario);
    }

    @Override
    public List<Usuario> listar() {
        return usuarios;
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarios.stream()
                .filter(usuario -> usuario.getId().getValor().equals(id))
                .findFirst()
                .orElse(null);
    }
}
