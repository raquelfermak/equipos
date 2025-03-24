package com.dekra.primerProyecto.proyecto.proyecto.API.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AsignacionDto {

    private String proyectoId;
    private String usuarioId;
    private String rolId;
    private String comentario;

    public AsignacionDto(String proyectoId, String usuarioId, String rolId) {
        this.proyectoId = proyectoId;
        this.usuarioId = usuarioId;
        this.rolId = rolId;

    }

    public AsignacionDto(String proyectoId, String usuarioId, String rolId, String comentario) {
        this.proyectoId = proyectoId;
        this.usuarioId = usuarioId;
        this.rolId = rolId;
        this.comentario = comentario;
    }
}
