package br.com.fiap.euroone_api.dto.turma;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TurmaCreateRequest {

    @NotBlank(message = "O código da turma é obrigatório")
    @Size(min = 2, max = 60)
    private String codigo;

    @Size(max = 60)
    private String periodo;

    @Size(max = 30)
    private String sala;

    @NotNull(message = "O id do curso é obrigatório")
    private Long cursoId;

    /**
     * Id do usuário com perfil EDUCADOR responsável pela turma (opcional).
     */
    private Long educadorId;
}
