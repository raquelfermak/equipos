package com.dekra.primerProyecto.proyecto.proyecto.API.dto;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import lombok.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ListarProyectoDto {

    private String id;
    private String nombre;
    private String email;
    private String descripcion;
    private Map<RolDto, Set<UsuarioDto>> asignaciones;

    public ListarProyectoDto(String nombre, String email, String descripcion) {
        this.nombre =  nombre;
        this.email = email;
        this.descripcion = descripcion;
    }


    public static ListarProyectoDto toDto(Proyecto proyecto) {
        if (proyecto == null) {
            return null;
        }

        ListarProyectoDto dto = new ListarProyectoDto();
        dto.setId(proyecto.getId() != null ? proyecto.getId().getValor() : null);
        dto.setNombre(proyecto.getNombre());
        dto.setEmail(proyecto.getEmail().getValor());
        dto.setDescripcion(proyecto.getDescripcion());

        if (proyecto.getAsignaciones() != null) {
            Map<RolDto, Set<UsuarioDto>> asignacionesMap = proyecto.getAsignaciones()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            entry -> RolDto.toDto(entry.getKey()),
                            entry -> entry.getValue()
                                    .stream()
                                    .map(UsuarioDto::toDto)
                                    .collect(Collectors.toSet())
                    ));
            dto.setAsignaciones(asignacionesMap);
        }

        return dto;
    }

}
