package com.dekra.primerProyecto.usuario.infrastrucure.listener;

import com.dekra.primerProyecto.usuario.application.event.UsuarioConsultaEvent;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.domain.repository.UsuarioRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConsultaEventListener {
    private final UsuarioRepository usuarioRepository;

    public UsuarioConsultaEventListener(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @EventListener
    public void handleUsuarioConsultaEvent(UsuarioConsultaEvent event) {
        boolean existe = usuarioRepository.buscarPorId(event.getUsuarioId()) != null;
        event.setExiste(existe);
    }
}
