package com.dekra.primerProyecto.proyecto.proyecto.API.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearActualizarProyectoDto {

    private String nombre;
    private String email;
    private String descripcion;
    private String comentario;

}
