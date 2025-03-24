package com.dekra.primerProyecto.shared.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;



@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Equipos",
                version = "0.0.0",
                description = "Equipos API"
        )
)

public class OpenApiConfig {
}
