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

import br.com.fiap.euroone_api.dto.recompensa.RecompensaCreateRequest;
import br.com.fiap.euroone_api.dto.recompensa.RecompensaMapper;
import br.com.fiap.euroone_api.dto.recompensa.RecompensaResponse;
import br.com.fiap.euroone_api.dto.recompensa.RecompensaUpdateRequest;
import br.com.fiap.euroone_api.service.RecompensaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/recompensas")
@Tag(name = "Recompensas", description = "Catálogo de recompensas resgatáveis por pontos")
public class RecompensaController {

    @Autowired
    private RecompensaService service;

    @Autowired
    private RecompensaMapper mapper;

    @PostMapping
    @Operation(summary = "Cria uma nova recompensa")
    public ResponseEntity<RecompensaResponse> create(@Valid @RequestBody RecompensaCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(mapper.toModel(dto))));
    }

    @GetMapping
    @Operation(summary = "Lista todas as recompensas")
    public ResponseEntity<List<RecompensaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma recompensa pelo id")
    public ResponseEntity<RecompensaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma recompensa existente")
    public ResponseEntity<RecompensaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RecompensaUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toModel(id, dto))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma recompensa pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
