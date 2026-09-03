package br.com.fiap.euroone_api.dto.matricula;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MatriculaUpdateRequest {

    @Min(value = 0, message = "O progresso mínimo é 0")
    @Max(value = 100, message = "O progresso máximo é 100")
    private Integer progresso;

    @Min(value = 0)
    private Integer pontos;
}
