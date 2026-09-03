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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.euroone_api.dto.resgate.ResgateCreateRequest;
import br.com.fiap.euroone_api.dto.resgate.ResgateMapper;
import br.com.fiap.euroone_api.dto.resgate.ResgateResponse;
import br.com.fiap.euroone_api.dto.resgate.ResgateUpdateRequest;
import br.com.fiap.euroone_api.service.ResgateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/resgates")
@Tag(name = "Resgates", description = "Resgates de recompensas com pontos acumulados na matrícula")
public class ResgateController {

    @Autowired
    private ResgateService service;

    @Autowired
    private ResgateMapper mapper;

    @PostMapping
    @Operation(summary = "Solicita o resgate de uma recompensa (debita pontos e reserva o item)")
    public ResponseEntity<ResgateResponse> solicitar(@Valid @RequestBody ResgateCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.solicitar(dto)));
    }

    @GetMapping
    @Operation(summary = "Lista resgates (opcionalmente filtrados por matrícula)")
    public ResponseEntity<List<ResgateResponse>> findAll(
            @RequestParam(value = "matriculaId", required = false) Long matriculaId) {
        return ResponseEntity.ok(service.findAll(matriculaId).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um resgate pelo id")
    public ResponseEntity<ResgateResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o status do resgate (APROVADO, ENTREGUE ou CANCELADO)")
    public ResponseEntity<ResgateResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody ResgateUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.atualizarStatus(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um resgate (devolve pontos e estoque se ainda não entregue)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
