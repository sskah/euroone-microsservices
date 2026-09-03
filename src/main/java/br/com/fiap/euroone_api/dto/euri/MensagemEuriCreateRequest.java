package br.com.fiap.euroone_api.dto.euri;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensagemEuriCreateRequest {

    @NotBlank(message = "O conteúdo da mensagem é obrigatório")
    @Size(min = 1, max = 2000, message = "A mensagem deve ter entre 1 e 2000 caracteres")
    private String conteudo;
}
