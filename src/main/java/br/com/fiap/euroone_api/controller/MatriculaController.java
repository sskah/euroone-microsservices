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

import br.com.fiap.euroone_api.dto.matricula.MatriculaCreateRequest;
import br.com.fiap.euroone_api.dto.matricula.MatriculaMapper;
import br.com.fiap.euroone_api.dto.matricula.MatriculaResponse;
import br.com.fiap.euroone_api.dto.matricula.MatriculaUpdateRequest;
import br.com.fiap.euroone_api.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/matriculas")
@Tag(name = "Matrículas", description = "Vínculo de educandos a turmas, com progresso e pontos")
public class MatriculaController {

    @Autowired
    private MatriculaService service;

    @Autowired
    private MatriculaMapper mapper;

    @PostMapping
    @Operation(summary = "Cria uma nova matrícula")
    public ResponseEntity<MatriculaResponse> create(@Valid @RequestBody MatriculaCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(dto)));
    }

    @GetMapping
    @Operation(summary = "Lista todas as matrículas")
    public ResponseEntity<List<MatriculaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma matrícula pelo id")
    public ResponseEntity<MatriculaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza progresso/pontos de uma matrícula")
    public ResponseEntity<MatriculaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MatriculaUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma matrícula pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
