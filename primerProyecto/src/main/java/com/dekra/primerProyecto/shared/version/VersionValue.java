package com.dekra.primerProyecto.shared.version;

import lombok.Getter;

@Getter
public class VersionValue {

    private int valor;

    private VersionValue(int valor){
        this.valor = valor;
    }

    public static VersionValue of(int valor){
        if(valor < 0){
            throw new IllegalArgumentException("La version no puede ser menor que 0");
        }
        return new VersionValue(valor);
    }

    public int getNextVersion() {
        return this.valor + 1;
    }

}
