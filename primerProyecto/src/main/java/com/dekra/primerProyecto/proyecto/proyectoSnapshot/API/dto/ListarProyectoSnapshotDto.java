package com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.dto;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model.ProyectoSnapshot;
import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ListarProyectoSnapshotDto {

    private String id;
    private String nombre;
    private String email;
    private String descripcion;
    private Map<RolDto, Set<UsuarioDto>> asignaciones;
    private int version;
    private LocalDateTime fechaCreacion;




    public static ListarProyectoSnapshotDto toDto(ProyectoSnapshot proyecto) {
        if (proyecto == null) {
            return null;
        }

        ListarProyectoSnapshotDto dto = new ListarProyectoSnapshotDto();
        dto.setId(proyecto.getId().getValor());
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

        dto.setVersion(proyecto.getVersionValue().getValor());
        dto.setFechaCreacion(proyecto.getLocalDateTime());

        return dto;
    }

}
