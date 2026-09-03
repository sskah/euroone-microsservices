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

import br.com.fiap.euroone_api.dto.missao.MissaoCreateRequest;
import br.com.fiap.euroone_api.dto.missao.MissaoMapper;
import br.com.fiap.euroone_api.dto.missao.MissaoResponse;
import br.com.fiap.euroone_api.dto.missao.MissaoUpdateRequest;
import br.com.fiap.euroone_api.service.MissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/missoes")
@Tag(name = "Missões", description = "Missões gamificadas atribuídas a educandos")
public class MissaoController {

    @Autowired
    private MissaoService service;

    @Autowired
    private MissaoMapper mapper;

    @PostMapping
    @Operation(summary = "Cria uma nova missão")
    public ResponseEntity<MissaoResponse> create(@Valid @RequestBody MissaoCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(dto)));
    }

    @GetMapping
    @Operation(summary = "Lista todas as missões")
    public ResponseEntity<List<MissaoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma missão pelo id")
    public ResponseEntity<MissaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma missão existente")
    public ResponseEntity<MissaoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MissaoUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma missão pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
