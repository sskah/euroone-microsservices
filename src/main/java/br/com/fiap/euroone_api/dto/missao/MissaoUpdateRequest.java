package br.com.fiap.euroone_api.dto.missao;

import java.time.LocalDate;

import br.com.fiap.euroone_api.model.enums.StatusMissao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MissaoUpdateRequest {

    @Size(min = 3, max = 100)
    private String titulo;

    @Size(max = 500)
    private String descricao;

    @Min(value = 1)
    private Integer pontos;

    private StatusMissao status;

    private LocalDate prazo;
}
