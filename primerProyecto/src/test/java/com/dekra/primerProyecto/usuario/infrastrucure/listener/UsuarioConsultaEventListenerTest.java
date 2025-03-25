package com.dekra.primerProyecto.usuario.infrastrucure.listener;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.usuario.application.event.UsuarioConsultaEvent;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import com.dekra.primerProyecto.usuario.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioConsultaEventListenerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioConsultaEventListener usuarioConsultaEventListener;

    @Test
    void handleUsuarioConsultaEvent_DeberiaDevolverTrue(){
        // GIVEN
        String usuarioId = "123";
        UsuarioConsultaEvent event = new UsuarioConsultaEvent(usuarioId);
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(new Usuario("Paco", EmailValue.of("paco@gmail.com")));

        // WHEN
        usuarioConsultaEventListener.handleUsuarioConsultaEvent(event);

        // THEN
        assertTrue(event.isExiste(),
                "Se esperaba que event.isExiste() fuera true," +
                        "porque el Usuario existe en el el repositorio");


    }

    @Test
    void handleUsuarioConsultaEvent_DeberiaDevolverFalse(){
        // GIVEN
        String usuarioId = "123";
        UsuarioConsultaEvent event = new UsuarioConsultaEvent(usuarioId);
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(null);

        // WHEN
        usuarioConsultaEventListener.handleUsuarioConsultaEvent(event);

        // THEN
        assertFalse(event.isExiste(),
                "Se esperaba que event.isExiste() fuera false," +
                        "porque el Usuario no existe en el el repositorio");


    }

}