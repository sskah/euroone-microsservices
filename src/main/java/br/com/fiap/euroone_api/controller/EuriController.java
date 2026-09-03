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

import br.com.fiap.euroone_api.dto.euri.ConversaEuriCreateRequest;
import br.com.fiap.euroone_api.dto.euri.ConversaEuriResponse;
import br.com.fiap.euroone_api.dto.euri.ConversaEuriUpdateRequest;
import br.com.fiap.euroone_api.dto.euri.EuriMapper;
import br.com.fiap.euroone_api.dto.euri.InteracaoEuriResponse;
import br.com.fiap.euroone_api.dto.euri.MensagemEuriCreateRequest;
import br.com.fiap.euroone_api.dto.euri.MensagemEuriResponse;
import br.com.fiap.euroone_api.model.ConversaEuri;
import br.com.fiap.euroone_api.model.MensagemEuri;
import br.com.fiap.euroone_api.service.EuriService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints da assistente Euri (mascote / chatbot do EuroOne).
 *
 * Fluxo típico:
 *   1) POST   /api/v1/euri/conversas                       → inicia uma conversa
 *   2) POST   /api/v1/euri/conversas/{id}/mensagens        → envia pergunta e recebe resposta
 *   3) GET    /api/v1/euri/conversas/{id}                  → recupera todo o histórico
 */
@RestController
@RequestMapping("/api/${api.version}/euri")
@Tag(name = "Euri (Chatbot)", description = "Assistente conversacional da plataforma EuroOne")
public class EuriController {

    @Autowired
    private EuriService service;

    @Autowired
    private EuriMapper mapper;

    // ---------- Conversas ----------

    @PostMapping("/conversas")
    @Operation(summary = "Inicia uma nova conversa com a Euri (com saudação automática)")
    public ResponseEntity<ConversaEuriResponse> iniciarConversa(
            @Valid @RequestBody ConversaEuriCreateRequest dto) {
        ConversaEuri conversa = service.iniciarConversa(dto);
        List<MensagemEuri> mensagens = service.listarMensagens(conversa.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDtoComMensagens(conversa, mensagens));
    }

    @GetMapping("/conversas")
    @Operation(summary = "Lista conversas (opcionalmente filtradas por usuário)")
    public ResponseEntity<List<ConversaEuriResponse>> listarConversas(
            @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
        List<ConversaEuriResponse> lista = service.listarConversas(usuarioId)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/conversas/{id}")
    @Operation(summary = "Recupera uma conversa e todas as suas mensagens")
    public ResponseEntity<ConversaEuriResponse> buscarConversa(@PathVariable Long id) {
        ConversaEuri conversa = service.findConversaById(id);
        List<MensagemEuri> mensagens = service.listarMensagens(id);
        return ResponseEntity.ok(mapper.toDtoComMensagens(conversa, mensagens));
    }

    @PutMapping("/conversas/{id}")
    @Operation(summary = "Atualiza uma conversa (renomear, encerrar ou reabrir)")
    public ResponseEntity<ConversaEuriResponse> atualizarConversa(
            @PathVariable Long id,
            @Valid @RequestBody ConversaEuriUpdateRequest dto) {
        return ResponseEntity.ok(mapper.toDto(service.atualizarConversa(id, dto)));
    }

    @DeleteMapping("/conversas/{id}")
    @Operation(summary = "Remove uma conversa e todo o seu histórico de mensagens")
    public ResponseEntity<Void> deletarConversa(@PathVariable Long id) {
        service.deletarConversa(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Mensagens ----------

    @PostMapping("/conversas/{conversaId}/mensagens")
    @Operation(summary = "Envia uma mensagem à Euri e recebe a resposta gerada")
    public ResponseEntity<InteracaoEuriResponse> enviarMensagem(
            @PathVariable Long conversaId,
            @Valid @RequestBody MensagemEuriCreateRequest dto) {
        MensagemEuri[] par = service.enviarMensagem(conversaId, dto);

        InteracaoEuriResponse resposta = new InteracaoEuriResponse();
        resposta.setMensagemUsuario(mapper.toDto(par[0]));
        resposta.setRespostaEuri(mapper.toDto(par[1]));
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/conversas/{conversaId}/mensagens")
    @Operation(summary = "Lista todas as mensagens de uma conversa em ordem cronológica")
    public ResponseEntity<List<MensagemEuriResponse>> listarMensagens(@PathVariable Long conversaId) {
        List<MensagemEuriResponse> lista = service.listarMensagens(conversaId)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(lista);
    }
}
