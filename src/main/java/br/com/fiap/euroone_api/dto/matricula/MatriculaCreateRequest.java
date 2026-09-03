package br.com.fiap.euroone_api.dto.matricula;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatriculaCreateRequest {

    @NotNull(message = "O id do educando é obrigatório")
    private Long educandoId;

    @NotNull(message = "O id da turma é obrigatório")
    private Long turmaId;

    @NotNull(message = "A data da matrícula é obrigatória")
    private LocalDate dataMatricula;

    @Min(value = 0, message = "O progresso mínimo é 0")
    @Max(value = 100, message = "O progresso máximo é 100")
    private Integer progresso = 0;

    @Min(value = 0)
    private Integer pontos = 0;
}
