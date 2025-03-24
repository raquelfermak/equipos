package com.dekra.primerProyecto.rol.application.event;

import com.dekra.primerProyecto.rol.domain.model.Rol;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RolConsultaEvent extends ApplicationEvent {

    private final String rolId;
    @Setter
    private Rol rol;

    public RolConsultaEvent(String rolId) {
        super(rolId);
        this.rolId = rolId;
    }
}
