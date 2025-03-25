package com.dekra.primerProyecto.rol.infrastrucure.listener;

import com.dekra.primerProyecto.rol.application.event.RolConsultaEvent;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.rol.domain.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolConsultaEventListenerTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolConsultaEventListener rolConsultaEventListener;


    @Test
    void handleRolConsultaEvent_deberiaDevolverTrue() {
        // GIVEN
        String rolId = "123";
        RolConsultaEvent event = new RolConsultaEvent(rolId);
        when(rolRepository.buscarPorId(rolId)).thenReturn(new Rol("developer"));

        // WHEN
        rolConsultaEventListener.handleRolConsultaEvent(event);

        // THEN
        assertTrue(event.isExiste(),
                "Se esperaba que event.isExiste() fuera true, " +
                        "porque el Rol existe en el repositorio");
    }

    @Test
    void handleRolConsultaEvent_deberiaDevolverFalse() {
        // GIVEN
        String rolId = "123";
        RolConsultaEvent event = new RolConsultaEvent(rolId);
        when(rolRepository.buscarPorId(rolId)).thenReturn(null);

        // WHEN
        rolConsultaEventListener.handleRolConsultaEvent(event);

        // THEN
        assertFalse(event.isExiste(),
                "Se esperaba que event.isExiste() fuera false, " +
                        "porque el Rol no existe en el repositorio");
    }
}