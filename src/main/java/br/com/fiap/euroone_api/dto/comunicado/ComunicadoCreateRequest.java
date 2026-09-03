package br.com.fiap.euroone_api.dto.comunicado;

import br.com.fiap.euroone_api.model.enums.TipoComunicado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComunicadoCreateRequest {

    @NotNull(message = "O id do remetente é obrigatório")
    private Long remetenteId;

    @NotNull(message = "O id do destinatário é obrigatório")
    private Long destinatarioId;

    @NotBlank(message = "O assunto é obrigatório")
    @Size(min = 3, max = 120)
    private String assunto;

    @NotBlank(message = "A mensagem é obrigatória")
    @Size(min = 3, max = 1000)
    private String mensagem;

    @NotNull(message = "O tipo é obrigatório (POSITIVO, ATENCAO ou CRITICO)")
    private TipoComunicado tipo;
}
