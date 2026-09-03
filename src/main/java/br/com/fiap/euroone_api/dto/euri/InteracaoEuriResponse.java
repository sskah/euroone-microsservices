package br.com.fiap.euroone_api.dto.euri;

import lombok.Data;

/**
 * Resposta ao envio de uma mensagem: devolve a mensagem do usuário
 * já persistida e a resposta gerada pela Euri, em um único objeto.
 */
@Data
public class InteracaoEuriResponse {
    private MensagemEuriResponse mensagemUsuario;
    private MensagemEuriResponse respostaEuri;
}
