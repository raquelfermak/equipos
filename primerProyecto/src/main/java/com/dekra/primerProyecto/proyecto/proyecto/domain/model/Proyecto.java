package com.dekra.primerProyecto.proyecto.proyecto.domain.model;

import java.util.Map;
import java.util.Set;

import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Proyecto {

    private IDValue id;
    private String nombre;
    private EmailValue email;
    private String descripcion;
    private Map<Rol, Set<Usuario>> asignaciones;

    public Proyecto(String nombre, EmailValue email, String descripcion){
        this.id  =  IDValue.of();
        this.nombre = nombre;
        this.email = email;
        this.descripcion = descripcion;

    }

}
