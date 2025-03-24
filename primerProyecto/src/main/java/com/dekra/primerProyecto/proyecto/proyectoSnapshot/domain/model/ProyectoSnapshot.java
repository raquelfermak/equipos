package com.dekra.primerProyecto.proyecto.proyectoSnapshot.domain.model;

import com.dekra.primerProyecto.proyecto.proyecto.domain.model.Proyecto;
import com.dekra.primerProyecto.rol.domain.model.Rol;
import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.version.VersionValue;
import com.dekra.primerProyecto.usuario.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ProyectoSnapshot extends Proyecto {
    private VersionValue versionValue;
    private LocalDateTime localDateTime;

    public ProyectoSnapshot(IDValue idValue, String nombre, EmailValue email, String descripcion,
                            Map<Rol, Set<Usuario>> asignaciones, VersionValue versionValue) {
        super(idValue, nombre, email, descripcion, asignaciones);
        this.versionValue = versionValue;
        this.localDateTime = LocalDateTime.now();
    }

}
