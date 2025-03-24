package com.dekra.primerProyecto.rol.API.controller;

import com.dekra.primerProyecto.rol.API.dto.RolDto;
import com.dekra.primerProyecto.rol.application.service.CrearRolService;
import com.dekra.primerProyecto.rol.application.service.ListarRolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Rol")
@RequestMapping("/rol")
public class RolController {

    private final CrearRolService crearRolService;
    private final ListarRolService listarRolService;


    public RolController(CrearRolService crearRolService, ListarRolService listarRolService) {
        this.crearRolService = crearRolService;
        this.listarRolService = listarRolService;
    }

    //crea un nuevo rol
    @PostMapping
    public RolDto crearRol(@RequestBody RolDto rolDto){
        return crearRolService.crearRol(rolDto);
    }

    //lista todos los roles existentes
    @GetMapping
    public List<RolDto> listarRoles(){
        return listarRolService.listar();
    }

}
