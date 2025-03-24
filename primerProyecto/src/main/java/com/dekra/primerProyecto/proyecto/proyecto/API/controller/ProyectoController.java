package com.dekra.primerProyecto.proyecto.proyecto.API.controller;

import com.dekra.primerProyecto.proyecto.proyecto.API.dto.AsignacionDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.CrearActualizarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.API.dto.ListarProyectoDto;
import com.dekra.primerProyecto.proyecto.proyecto.application.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Proyecto")
@RequestMapping("/proyecto")
public class ProyectoController {

    private final CrearProyectoService crearProyectoService;
    private final ListarProyectoService listarProyectoService;
    private final AsignacionProyectoService asignacionProyectoService;
    private final DesasignacionProyectoService desasignacionProyectoService;
    private final ActualizarProyectoService actualizarProyectoService;


    public ProyectoController(CrearProyectoService crearProyectoService, ListarProyectoService listarProyectoService, AsignacionProyectoService asignacionProyectoService, DesasignacionProyectoService desasignacionProyectoService, ActualizarProyectoService actualizarProyectoService) {
        this.crearProyectoService = crearProyectoService;
        this.listarProyectoService = listarProyectoService;
        this.asignacionProyectoService = asignacionProyectoService;
        this.desasignacionProyectoService = desasignacionProyectoService;
        this.actualizarProyectoService = actualizarProyectoService;
    }

    //crea un nuevo proyecto

    @PostMapping
    public ListarProyectoDto crearProyecto(@RequestBody CrearActualizarProyectoDto crearActualizarProyectoDto){
        return crearProyectoService.crearProyecto(crearActualizarProyectoDto);
    }
    
    //lista todos los proyectos existentes
    @GetMapping
    public List<ListarProyectoDto> listarProyectos(){
        return listarProyectoService.listar();
    }

    //asigna un usuario a un rol en un proyecto
    @PostMapping("/asignar")
    public ListarProyectoDto asignarUsuarioARol(@RequestBody AsignacionDto asignacionDto){
        return asignacionProyectoService.asignarUsuarioARol(asignacionDto);
    }

    //asigna un usuario a un rol en un proyecto
    @PostMapping("/desasignar")
    public ListarProyectoDto desasignarUsuarioARol(@RequestBody AsignacionDto asignacionDto){
        return desasignacionProyectoService.desasignarUsuarioARol(asignacionDto);
    }

    @PutMapping("/{idProyecto}")
    public ListarProyectoDto actualizarProyecto(@PathVariable String idProyecto, @RequestBody CrearActualizarProyectoDto crearActualizarProyectoDto){
        return actualizarProyectoService.actualizarProyecto(idProyecto, crearActualizarProyectoDto);
    }
    
}
