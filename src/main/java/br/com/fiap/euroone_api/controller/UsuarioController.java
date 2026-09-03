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

import br.com.fiap.euroone_api.dto.usuario.UsuarioCreateRequest;
import br.com.fiap.euroone_api.dto.usuario.UsuarioMapper;
import br.com.fiap.euroone_api.dto.usuario.UsuarioResponse;
import br.com.fiap.euroone_api.dto.usuario.UsuarioUpdateRequest;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import br.com.fiap.euroone_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários (educandos, educadores e gestão)")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioMapper mapper;

    @PostMapping
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioCreateRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(service.create(mapper.toModel(dto))));
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários (opcionalmente filtrando por perfil)")
    public ResponseEntity<List<UsuarioResponse>> findAll(
            @RequestParam(value = "perfil", required = false) PerfilUsuario perfil) {
        List<UsuarioResponse> lista = (perfil == null ? service.findAll() : service.findByPerfil(perfil))
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo id")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário existente")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.toModel(id, dto))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um usuário pelo id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
