package com.dekra.primerProyecto.shared.email.domain.repository;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;

import java.util.Set;

public interface EmailValueRepository {
    void guardar(EmailValue emailValue);
    Set<String> listarDominios();
}
