package com.dekra.primerProyecto.rol.API.dto;

import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.id.IDValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolDtoTest {

        @Test
        public void toDto_deberiaRetornarNullSiRolEsNull() {
        // GIVEN
        Rol rol = null;

        // WHEN
        RolDto resultado = RolDto.toDto(rol);

        // THEN
        assertNull(resultado, "Se esperaba que el DTO fuera null cuando el rol es null");
        }

        @Test
        public void toDto_deberiaConvertirRolADtoCorrectamente() {
            // GIVEN
            Rol rolMock = mock(Rol.class);
            when(rolMock.getNombre()).thenReturn("Administrador");
            // Se simula el id, aunque en esta prueba no se realizará aserción sobre él
            IDValue idDummy = IDValue.of();
            when(rolMock.getId()).thenReturn(idDummy);

            // WHEN
            RolDto dto = RolDto.toDto(rolMock);

            // THEN
            assertNotNull(dto, "El DTO no debería ser null cuando el rol es válido");
            assertNotNull(dto.getId(), "El id no debería ser null cuando el rol es válido");
            assertEquals("Administrador", dto.getNombre(), "El nombre del DTO debe coincidir con el nombre del rol");
    }
}