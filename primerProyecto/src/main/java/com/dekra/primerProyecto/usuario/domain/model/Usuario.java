package com.dekra.primerProyecto.usuario.domain.model;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import lombok.*;



@Getter
public class Usuario {


    private IDValue id;
    private String nombre;
    private EmailValue email;

    public Usuario(String nombre, EmailValue email){
        this.id  =  IDValue.of();
        this.nombre = nombre;
        this.email = email;
    }


}
