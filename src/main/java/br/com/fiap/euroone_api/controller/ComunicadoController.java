package br.com.fiap.euroone_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.euroone_api.dto.comunicado.ComunicadoCreateRequest;
import br.com.fiap.euroone_api.dto.comunicado.ComunicadoMapper;
import br.com.fiap.euroone_api.dto.comunicado.ComunicadoResponse;
import br.com.fiap.euroone_api.dto.comunicado.ComunicadoUpdateRequest;
import br.com.fiap.euroone_api.service.ComunicadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/comunicados")
@Tag(name = "Comunicados", description = "Comunicações internas entre usuários da plataforma")
public class ComunicadoController {

    @Autowired
    private ComunicadoService service;

    @Autowired
    private ComunicadoMapper mapper;

    @PostMapping
    @Operation(summary = "Envia um novo comunicado interno")
    public ResponseEntity<ComunicadoResponse> create(@Valid @RequestBody ComunicadoCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(dto)));
    }

    @GetMapping
    @Operation(summary = "Lista todos os comunicados")
    public ResponseEntity<List<ComunicadoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um comunicado pelo id")
    public ResponseEntity<ComunicadoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um comunicado (ex.: marcar como lido)")
    public ResponseEntity<ComunicadoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ComunicadoUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um comunicado pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
