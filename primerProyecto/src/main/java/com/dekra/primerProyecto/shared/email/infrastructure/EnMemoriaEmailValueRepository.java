package com.dekra.primerProyecto.shared.email.infrastructure;

import com.dekra.primerProyecto.shared.email.domain.model.EmailValue;
import com.dekra.primerProyecto.shared.email.domain.repository.EmailValueRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EnMemoriaEmailValueRepository implements EmailValueRepository {

    List<EmailValue> emailValues = new ArrayList<>();

    @Override
    public void guardar(EmailValue emailValue) {
        if (!emailValues.contains(emailValue)) {
           emailValues.add(emailValue);
        }
    }

    @Override
    public Set<String> listarDominios() {
        return emailValues.stream()
                .map(EmailValue::getDomain)
                .collect(Collectors.toSet());
    }
}
