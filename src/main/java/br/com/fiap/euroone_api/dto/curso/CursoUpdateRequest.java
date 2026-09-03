package br.com.fiap.euroone_api.dto.curso;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CursoUpdateRequest {

    @Size(min = 3, max = 100)
    private String nome;

    @Size(max = 60)
    private String trilha;

    @Min(value = 1)
    private Integer cargaHoraria;

    @Size(max = 500)
    private String descricao;
}
