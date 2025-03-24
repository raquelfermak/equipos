package com.dekra.primerProyecto.rol.domain.model;

import com.dekra.primerProyecto.shared.id.IDValue;
import lombok.*;


@Getter
public class Rol {

    private IDValue id;
    private String nombre;

    public Rol(String nombre){
        this.id  =  IDValue.of();
        this.nombre = nombre;
    }

}
