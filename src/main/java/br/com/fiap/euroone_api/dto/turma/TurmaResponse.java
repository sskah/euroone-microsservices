package br.com.fiap.euroone_api.dto.turma;

import br.com.fiap.euroone_api.dto.curso.CursoResponse;
import br.com.fiap.euroone_api.dto.usuario.UsuarioResponse;
import lombok.Data;

@Data
public class TurmaResponse {
    private Long id;
    private String codigo;
    private String periodo;
    private String sala;
    private CursoResponse curso;
    private UsuarioResponse educador;
}
