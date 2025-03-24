package com.dekra.primerProyecto.shared.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionValueTest {

    @Test
    void of_deberiaCrearInstanciaConValorValido() {
        // GIVEN
        int valorInicial = 5;
        // WHEN
        VersionValue version = VersionValue.of(valorInicial);
        // THEN
        assertNotNull(version, "La instancia no debe ser nula");
        assertEquals(valorInicial, version.getValor(), "El valor debe coincidir con el inicial");
    }

    @Test
    void of_deberiaLanzarExcepcionCuandoValorEsNegativo() {
        // GIVEN
        int valorNegativo = -1;
        // WHEN / THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> VersionValue.of(valorNegativo),
                "Debe lanzar excepción para valores negativos");
        assertEquals("La version no puede ser menor que 0", exception.getMessage(), "El mensaje de la excepción debe coincidir");
    }

    @Test
    void getNextVersion_deberiaRetornarValorIncrementado() {
        // GIVEN
        int valorInicial = 10;
        VersionValue version = VersionValue.of(valorInicial);
        // WHEN
        int siguienteVersion = version.getNextVersion();
        // THEN
        assertEquals(valorInicial + 1, siguienteVersion, "La siguiente versión debe ser el valor inicial incrementado en 1");
    }
}