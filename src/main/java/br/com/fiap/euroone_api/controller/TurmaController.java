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

import br.com.fiap.euroone_api.dto.turma.TurmaCreateRequest;
import br.com.fiap.euroone_api.dto.turma.TurmaMapper;
import br.com.fiap.euroone_api.dto.turma.TurmaResponse;
import br.com.fiap.euroone_api.dto.turma.TurmaUpdateRequest;
import br.com.fiap.euroone_api.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/turmas")
@Tag(name = "Turmas", description = "Gerenciamento de turmas vinculadas a cursos e educadores")
public class TurmaController {

    @Autowired
    private TurmaService service;

    @Autowired
    private TurmaMapper mapper;

    @PostMapping
    @Operation(summary = "Cria uma nova turma")
    public ResponseEntity<TurmaResponse> create(@Valid @RequestBody TurmaCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(dto)));
    }

    @GetMapping
    @Operation(summary = "Lista todas as turmas")
    public ResponseEntity<List<TurmaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma turma pelo id")
    public ResponseEntity<TurmaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma turma existente")
    public ResponseEntity<TurmaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TurmaUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma turma pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
