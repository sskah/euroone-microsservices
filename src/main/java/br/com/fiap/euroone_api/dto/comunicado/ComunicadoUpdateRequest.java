package br.com.fiap.euroone_api.dto.comunicado;

import br.com.fiap.euroone_api.model.enums.TipoComunicado;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComunicadoUpdateRequest {

    @Size(min = 3, max = 120)
    private String assunto;

    @Size(min = 3, max = 1000)
    private String mensagem;

    private TipoComunicado tipo;

    private Boolean lido;
}
