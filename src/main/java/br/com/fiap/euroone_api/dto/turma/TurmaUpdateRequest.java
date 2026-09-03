package br.com.fiap.euroone_api.dto.turma;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TurmaUpdateRequest {

    @Size(min = 2, max = 60)
    private String codigo;

    @Size(max = 60)
    private String periodo;

    @Size(max = 30)
    private String sala;

    private Long cursoId;
    private Long educadorId;
}
