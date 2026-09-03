package br.com.fiap.euroone_api.dto.missao;

import java.time.LocalDate;

import br.com.fiap.euroone_api.dto.usuario.UsuarioResponse;
import br.com.fiap.euroone_api.model.enums.StatusMissao;
import lombok.Data;

@Data
public class MissaoResponse {
    private Long id;
    private String titulo;
    private String descricao;
    private Integer pontos;
    private StatusMissao status;
    private LocalDate prazo;
    private UsuarioResponse educando;
}
