package com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.controller;

import com.dekra.primerProyecto.proyecto.proyectoSnapshot.API.dto.ListarProyectoSnapshotDto;
import com.dekra.primerProyecto.proyecto.proyectoSnapshot.application.ListarProyectoSnapshotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "ProyectoSnapshot")
@RequestMapping("/proyectoSnapshot")
public class ProyectoSnapshotController {

    private final ListarProyectoSnapshotService listarProyectoSnapshotService;


    public ProyectoSnapshotController(ListarProyectoSnapshotService listarProyectoSnapshotService) {
        this.listarProyectoSnapshotService = listarProyectoSnapshotService;
    }

    
    //lista todos los proyectoSnapshots existentes
    @GetMapping
    public List<ListarProyectoSnapshotDto> listarProyectoSnapshots(){
        return listarProyectoSnapshotService.listar();
    }

    
}
