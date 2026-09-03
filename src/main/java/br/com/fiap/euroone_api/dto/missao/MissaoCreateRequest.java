package br.com.fiap.euroone_api.dto.missao;

import java.time.LocalDate;

import br.com.fiap.euroone_api.model.enums.StatusMissao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MissaoCreateRequest {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100)
    private String titulo;

    @Size(max = 500)
    private String descricao;

    @NotNull(message = "Os pontos são obrigatórios")
    @Min(value = 1, message = "A missão deve valer pelo menos 1 ponto")
    private Integer pontos;

    private StatusMissao status;

    @NotNull(message = "O prazo é obrigatório")
    private LocalDate prazo;

    @NotNull(message = "O id do educando é obrigatório")
    private Long educandoId;
}
