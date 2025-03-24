package com.dekra.primerProyecto.shared.email.API;


import com.dekra.primerProyecto.shared.email.infrastructure.EnMemoriaEmailValueRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/emailValue")
public class EmailValueController {

    private final EnMemoriaEmailValueRepository enMemoriaEmailValueRepository;

    public EmailValueController(EnMemoriaEmailValueRepository enMemoriaEmailValueRepository) {
        this.enMemoriaEmailValueRepository = enMemoriaEmailValueRepository;
    }

    @GetMapping
    public Set<String> listarDominios(){
        return enMemoriaEmailValueRepository.listarDominios();
    }
}
