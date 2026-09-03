package br.com.fiap.euroone_api.dto.presenca;

import java.time.LocalDate;

import br.com.fiap.euroone_api.dto.matricula.MatriculaResponse;
import lombok.Data;

@Data
public class PresencaResponse {
    private Long id;
    private MatriculaResponse matricula;
    private LocalDate data;
    private Boolean presente;
    private String observacao;
}
