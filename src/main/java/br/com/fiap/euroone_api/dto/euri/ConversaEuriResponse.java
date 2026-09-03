package br.com.fiap.euroone_api.dto.euri;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fiap.euroone_api.dto.usuario.UsuarioResponse;
import lombok.Data;

@Data
public class ConversaEuriResponse {
    private Long id;
    private UsuarioResponse usuario;
    private String titulo;
    private LocalDateTime iniciadaEm;
    private Boolean ativa;

    /**
     * Preenchido apenas na consulta detalhada (GET /conversas/{id}).
     * No listão (GET /conversas) fica null para não sobrecarregar a resposta.
     */
    private List<MensagemEuriResponse> mensagens;
}
