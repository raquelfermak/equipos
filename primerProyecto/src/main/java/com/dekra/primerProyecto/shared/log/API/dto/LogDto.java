package com.dekra.primerProyecto.shared.log.API.dto;

import com.dekra.primerProyecto.shared.id.IDValue;
import com.dekra.primerProyecto.shared.log.domain.model.Log;
import com.dekra.primerProyecto.shared.log.domain.model.TipoOperacion;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LogDto {

    private IDValue id;
    private IDValue idProyecto;
    private IDValue idUsuario;
    private LocalDate fechaModificacion;
    private LocalTime horaModificacion;
    private TipoOperacion tipoOperacion;
    private String comentario;

    public static LogDto toDto(Log log){
        LogDto logDto = new LogDto();
        logDto.setId(log.getId());
        logDto.setIdProyecto(log.getIdProyecto());
        logDto.setIdUsuario(log.getIdUsuario() != null ? log.getIdUsuario() : null);
        logDto.setFechaModificacion(log.getFechaModificacion());
        logDto.setHoraModificacion(log.getHoraModificacion());
        logDto.setTipoOperacion(log.getTipoOperacion());
        logDto.setComentario(log.getComentario() != null ? log.getComentario() : null);
        return  logDto;
    }
}
