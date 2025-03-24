package com.dekra.primerProyecto.usuario.API.controller;

import com.dekra.primerProyecto.usuario.application.service.CrearUsuarioService;
import com.dekra.primerProyecto.usuario.application.service.ListarUsuarioService;
import com.dekra.primerProyecto.usuario.API.dto.UsuarioDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Usuario")
@RequestMapping("/usuario")
public class UsuarioController {

    private final CrearUsuarioService crearUsuarioService;
    private final ListarUsuarioService listarUsuarioService;


    public UsuarioController(CrearUsuarioService crearUsuarioService, ListarUsuarioService listarUsuarioService) {
        this.crearUsuarioService = crearUsuarioService;
        this.listarUsuarioService = listarUsuarioService;
    }

    //crea un nuevo usuario
    @PostMapping
    public UsuarioDto crearUsuario(@RequestBody UsuarioDto usuarioDto){
        return crearUsuarioService.crearUsuario(usuarioDto);
    }
    
    //lista todos los usuarios existentes
    @GetMapping
    public List<UsuarioDto> listarUsuarios(){
        return listarUsuarioService.listar();
    }

}
