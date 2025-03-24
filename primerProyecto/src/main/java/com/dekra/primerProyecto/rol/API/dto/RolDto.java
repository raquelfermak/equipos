package com.dekra.primerProyecto.rol.API.dto;

import com.dekra.primerProyecto.rol.domain.model.Rol;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RolDto {

    private String id;
    private String nombre;

    public RolDto(String nombre) {
        this.nombre = nombre;
    }

    public static RolDto toDto(Rol rol){
        if (rol == null){
            return null;
        }

        RolDto dto = new RolDto();
        dto.setId(rol.getId().getValor());
        dto.setNombre(rol.getNombre());
        return dto;
    }
}
