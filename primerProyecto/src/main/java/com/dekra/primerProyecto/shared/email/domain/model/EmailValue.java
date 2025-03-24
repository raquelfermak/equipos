package com.dekra.primerProyecto.shared.email.domain.model;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.Objects;

@Getter
public final class EmailValue {

    private final String valor;


    private EmailValue(String value) {
        if (value == null || !value.contains("@")) {
            throw new IllegalArgumentException("Formato de email no valido: " + value);
        }
        this.valor = value;
    }

    public static EmailValue of(String value) {
        return new EmailValue(value);
    }

    public String getDomain() {
        return valor.substring(valor.indexOf("@") + 1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailValue)) return false;
        EmailValue that = (EmailValue) o;
        return Objects.equals(valor, that.valor);
    }

}