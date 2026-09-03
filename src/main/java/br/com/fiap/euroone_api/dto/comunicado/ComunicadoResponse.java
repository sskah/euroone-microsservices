package br.com.fiap.euroone_api.dto.comunicado;

import java.time.LocalDateTime;

import br.com.fiap.euroone_api.dto.usuario.UsuarioResponse;
import br.com.fiap.euroone_api.model.enums.TipoComunicado;
import lombok.Data;

@Data
public class ComunicadoResponse {
    private Long id;
    private UsuarioResponse remetente;
    private UsuarioResponse destinatario;
    private String assunto;
    private String mensagem;
    private TipoComunicado tipo;
    private LocalDateTime dataEnvio;
    private Boolean lido;
}
