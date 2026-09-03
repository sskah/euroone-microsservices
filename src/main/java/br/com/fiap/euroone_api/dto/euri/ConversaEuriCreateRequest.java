package br.com.fiap.euroone_api.dto.euri;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConversaEuriCreateRequest {

    @NotNull(message = "O id do usuário é obrigatório")
    private Long usuarioId;

    /**
     * Título opcional para a conversa. Se não informado, será gerado
     * automaticamente pelo service (ex.: "Conversa de 30/08/2025 14:22").
     */
    @Size(max = 120)
    private String titulo;
}
