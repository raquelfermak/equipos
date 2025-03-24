package com.dekra.primerProyecto.rol.infrastrucure.listener;

import com.dekra.primerProyecto.rol.application.event.RolConsultaEvent;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.domain.repository.RolRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RolConsultaEventListener {

    private final RolRepository rolRepository;

    public RolConsultaEventListener(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @EventListener
    public void handleRolConsultaEvent(RolConsultaEvent event) {
        boolean existe = rolRepository.buscarPorId(event.getRolId()) != null;
        event.setExiste(existe);
    }
}
