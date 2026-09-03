package br.com.fiap.euroone_api.dto.curso;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CursoCreateRequest {

    @NotBlank(message = "O nome do curso é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @Size(max = 60)
    private String trilha;

    @NotNull(message = "A carga horária é obrigatória")
    @Min(value = 1, message = "A carga horária deve ser maior que zero")
    private Integer cargaHoraria;

    @Size(max = 500)
    private String descricao;
}
