package com.dekra.primerProyecto.usuario.application.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UsuarioConsultaEvent extends ApplicationEvent {

    private final String usuarioId;
    @Setter
    private boolean existe;

    public UsuarioConsultaEvent(String usuarioId) {
        super(usuarioId);
        this.usuarioId = usuarioId;
    }


}
