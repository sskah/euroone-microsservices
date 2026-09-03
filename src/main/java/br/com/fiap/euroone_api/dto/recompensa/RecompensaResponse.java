package br.com.fiap.euroone_api.dto.recompensa;

import lombok.Data;

@Data
public class RecompensaResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Integer custoPontos;
    private Integer estoque;
    private Boolean disponivel;
}
