package com.dekra.primerProyecto.shared.log.domain.model;

import com.dekra.primerProyecto.shared.id.IDValue;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Log {

    private IDValue id;
    private IDValue idProyecto;
    private IDValue idUsuario;
    private LocalDate fechaModificacion;
    private LocalTime horaModificacion;
    private TipoOperacion tipoOperacion;
    private String comentario;

    public Log(IDValue idProyecto, IDValue idUsuario, TipoOperacion tipoOperacion, String comentario) {
        this.id = IDValue.of();
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.fechaModificacion = LocalDate.now();
        this.horaModificacion = LocalTime.now();
        this.tipoOperacion = tipoOperacion;
        this.comentario = comentario;
    }

}
