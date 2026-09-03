package br.com.fiap.euroone_api.dto.euri;

import java.time.LocalDateTime;

import br.com.fiap.euroone_api.model.enums.RemetenteEuri;
import lombok.Data;

@Data
public class MensagemEuriResponse {
    private Long id;
    private RemetenteEuri remetente;
    private String conteudo;
    private LocalDateTime enviadaEm;
}
