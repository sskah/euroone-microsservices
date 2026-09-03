package br.com.fiap.euroone_api.dto.presenca;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PresencaUpdateRequest {

    private Boolean presente;

    @Size(max = 250)
    private String observacao;
}
