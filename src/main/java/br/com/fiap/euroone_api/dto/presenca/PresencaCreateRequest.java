package br.com.fiap.euroone_api.dto.presenca;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PresencaCreateRequest {

    @NotNull(message = "O id da matrícula é obrigatório")
    private Long matriculaId;

    @NotNull(message = "A data é obrigatória")
    private LocalDate data;

    @NotNull(message = "É preciso informar se o educando esteve presente")
    private Boolean presente;

    @Size(max = 250)
    private String observacao;
}
