package com.dekra.primerProyecto.shared.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode // Lombok genera equals y hashCode en base a los campos
public final class IDValue {

    private final String valor;

    private IDValue(String valor) {
        this.valor = valor;
    }

    public static IDValue of() {
        return new IDValue(UUID.randomUUID().toString());
    }

    public static IDValue of(String id) {
        return new IDValue(id);
    }
}
