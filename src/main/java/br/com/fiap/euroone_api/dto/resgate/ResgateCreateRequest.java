package br.com.fiap.euroone_api.dto.resgate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResgateCreateRequest {

    @NotNull(message = "O id da matrícula é obrigatório")
    private Long matriculaId;

    @NotNull(message = "O id da recompensa é obrigatório")
    private Long recompensaId;
}
