package com.dekra.primerProyecto.shared.email.infrastructure;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnMemoriaEmailValueRepositoryTest {

    private EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    @BeforeEach
    void setUp(){
        enMemoriaEmailValueRepository = new EnMemoriaEmailValueRepository();
    }

    @Test
    void guardarNuevoEmail_deberiaGuardarEmailYListarDominioUnico() {
        // GIVEN
        EmailValue email = EmailValue.of("prueba@dominio.com");

        // WHEN
        enMemoriaEmailValueRepository.guardar(email);
        Set<String> dominios = enMemoriaEmailValueRepository.listarDominios();

        // THEN
        assertTrue(dominios.contains("dominio.com"), "El dominio 'dominio.com' debe estar presente.");
        assertEquals(1, dominios.size(), "Solo debe haber un dominio en la lista.");
    }

    @Test
    void guardarEmailDuplicado_noDeberiaDuplicarDominio() {
        // GIVEN
        EmailValue email1 = EmailValue.of("prueba@dominio.com");
        EmailValue email2 = EmailValue.of("prueba@dominio.com");

        // WHEN
        enMemoriaEmailValueRepository.guardar(email1);
        enMemoriaEmailValueRepository.guardar(email2);
        Set<String> dominios = enMemoriaEmailValueRepository.listarDominios();

        // THEN
        assertTrue(dominios.contains("dominio.com"), "El dominio 'dominio.com' debe estar presente.");
        assertEquals(1, dominios.size(), "No se deben duplicar los dominios al guardar emails iguales.");
    }

    @Test
    void listarDominiosConMultiplesEmails_deberiaRetornarDominiosUnicos() {
        // GIVEN
        EmailValue email1 = EmailValue.of("prueba@dominio.com");
        EmailValue email2 = EmailValue.of("usuario@otro.com");
        EmailValue email3 = EmailValue.of("info@dominio.com");

        // WHEN
        enMemoriaEmailValueRepository.guardar(email1);
        enMemoriaEmailValueRepository.guardar(email2);
        enMemoriaEmailValueRepository.guardar(email3);
        Set<String> dominios = enMemoriaEmailValueRepository.listarDominios();

        // THEN
        assertTrue(dominios.contains("dominio.com"), "El dominio 'dominio.com' debe estar presente.");
        assertTrue(dominios.contains("otro.com"), "El dominio 'otro.com' debe estar presente.");
        assertEquals(2, dominios.size(), "Se deben listar únicamente dos dominios únicos.");
    }
}