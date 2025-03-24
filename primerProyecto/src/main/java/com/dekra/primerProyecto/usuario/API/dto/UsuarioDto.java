package com.dekra.primerProyecto.usuario.API.dto;

import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDto {

    private String id;
    private String nombre;
    private String email;

    public UsuarioDto(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }


    public static UsuarioDto toDto(Usuario usuario){
        if (usuario == null){
            return null;
        }

        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId().getValor());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail().getValor());
        return dto;
    }

}
