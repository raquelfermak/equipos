package com.dekra.primerProyecto.usuario.application.event;

import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UsuarioConsultaEvent extends ApplicationEvent {

    private final String usuarioId;
    @Setter
    private Usuario usuario;

    public UsuarioConsultaEvent(String usuarioId) {
        super(usuarioId);
        this.usuarioId = usuarioId;
    }

}
