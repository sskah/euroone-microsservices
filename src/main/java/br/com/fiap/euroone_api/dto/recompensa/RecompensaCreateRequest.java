package br.com.fiap.euroone_api.dto.recompensa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecompensaCreateRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @Size(max = 500)
    private String descricao;

    @NotNull(message = "O custo em pontos é obrigatório")
    @Min(value = 1, message = "O custo deve ser maior que zero")
    private Integer custoPontos;

    @NotNull(message = "O estoque é obrigatório")
    @Min(value = 0, message = "O estoque não pode ser negativo")
    private Integer estoque;

    private Boolean disponivel;
}
