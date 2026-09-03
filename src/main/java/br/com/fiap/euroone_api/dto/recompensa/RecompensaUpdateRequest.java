package br.com.fiap.euroone_api.dto.recompensa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecompensaUpdateRequest {

    @Size(min = 3, max = 100)
    private String nome;

    @Size(max = 500)
    private String descricao;

    @Min(value = 1)
    private Integer custoPontos;

    @Min(value = 0)
    private Integer estoque;

    private Boolean disponivel;
}
