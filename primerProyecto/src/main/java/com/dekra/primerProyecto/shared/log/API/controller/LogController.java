package com.dekra.primerProyecto.shared.log.API.controller;

import com.dekra.primerProyecto.shared.log.API.dto.LogDto;
import com.dekra.primerProyecto.shared.log.application.ListarLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Log")
@RequestMapping("/log")
public class LogController {

    private final ListarLogService listarLogService;


    public LogController(ListarLogService listarLogService) {
        this.listarLogService = listarLogService;
    }

    
    //lista todos los logs existentes
    @GetMapping
    public List<LogDto> listarLogs(){
        List<LogDto> logDtos = listarLogService.listar();
        return logDtos;
    }

    
}
