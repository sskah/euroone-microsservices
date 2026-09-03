package br.com.fiap.euroone_api.dto.euri;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConversaEuriUpdateRequest {

    @Size(max = 120)
    private String titulo;

    /**
     * Permite encerrar (false) ou reabrir (true) uma conversa.
     */
    private Boolean ativa;
}
