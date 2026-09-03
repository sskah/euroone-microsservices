package br.com.fiap.euroone_api.dto.curso;

import lombok.Data;

@Data
public class CursoResponse {
    private Long id;
    private String nome;
    private String trilha;
    private Integer cargaHoraria;
    private String descricao;
}
