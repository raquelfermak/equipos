package com.dekra.primerProyecto.shared.email.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValueTest {

    @Test
    void crearEmailValue_deberiaCrearEmailValidoCuandoFormatoEsCorrecto() {
       // GIVEN / WHEN
        EmailValue email = EmailValue.of("test@example.com");

        // THEN
        assertNotNull(email, "El EmailValue no debe ser nulo");
        assertEquals("test@example.com", email.getValor(), "El valor del email debe coincidir");
        assertEquals("example.com", email.getDomain(), "El dominio debe ser 'example.com'");
    }

    @Test
    void crearEmailValue_deberiaLanzarExcepcionCuandoNoContieneArroba() {
        // GIVEN / WHEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmailValue.of("testexample.com");
        });

        // THEN
        assertEquals("Formato de email no valido: testexample.com", exception.getMessage());
    }

    @Test
    void crearEmailValue_deberiaLanzarExcepcionCuandoEmailEsNull() {
        // GIVEN / WHEN /THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmailValue.of(null);
        });
        assertEquals("Formato de email no valido: null", exception.getMessage());
    }

    @Test
    void equals_deberiaRetornarVerdaderoParaEmailsIgualesYFalsoParaEmailsDiferentes() {
        // GIVEN / WHEN
        EmailValue email1 = EmailValue.of("test@example.com");
        EmailValue email2 = EmailValue.of("test@example.com");
        EmailValue email3 = EmailValue.of("otro@example.com");

        // THEN
        assertEquals(email1, email2, "Los emails iguales deben ser considerados iguales");
        assertNotEquals(email1, email3, "Emails diferentes no deben ser iguales");
    }

}