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

import br.com.fiap.euroone_api.dto.curso.CursoCreateRequest;
import br.com.fiap.euroone_api.dto.curso.CursoMapper;
import br.com.fiap.euroone_api.dto.curso.CursoResponse;
import br.com.fiap.euroone_api.dto.curso.CursoUpdateRequest;
import br.com.fiap.euroone_api.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/cursos")
@Tag(name = "Cursos", description = "Gerenciamento do catálogo de cursos da plataforma")
public class CursoController {

    @Autowired
    private CursoService service;

    @Autowired
    private CursoMapper mapper;

    @PostMapping
    @Operation(summary = "Cria um novo curso")
    public ResponseEntity<CursoResponse> create(@Valid @RequestBody CursoCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(mapper.toModel(dto))));
    }

    @GetMapping
    @Operation(summary = "Lista todos os cursos")
    public ResponseEntity<List<CursoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um curso pelo id")
    public ResponseEntity<CursoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um curso existente")
    public ResponseEntity<CursoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CursoUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toModel(id, dto))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um curso pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
