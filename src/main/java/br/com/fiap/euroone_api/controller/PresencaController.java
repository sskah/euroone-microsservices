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

import br.com.fiap.euroone_api.dto.presenca.PresencaCreateRequest;
import br.com.fiap.euroone_api.dto.presenca.PresencaMapper;
import br.com.fiap.euroone_api.dto.presenca.PresencaResponse;
import br.com.fiap.euroone_api.dto.presenca.PresencaUpdateRequest;
import br.com.fiap.euroone_api.service.PresencaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/presencas")
@Tag(name = "Presenças", description = "Registro de presença/falta por matrícula e data")
public class PresencaController {

    @Autowired
    private PresencaService service;

    @Autowired
    private PresencaMapper mapper;

    @PostMapping
    @Operation(summary = "Registra uma presença/falta")
    public ResponseEntity<PresencaResponse> create(@Valid @RequestBody PresencaCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(dto)));
    }

    @GetMapping
    @Operation(summary = "Lista todos os registros de presença")
    public ResponseEntity<List<PresencaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um registro de presença pelo id")
    public ResponseEntity<PresencaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um registro de presença")
    public ResponseEntity<PresencaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PresencaUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um registro de presença")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
