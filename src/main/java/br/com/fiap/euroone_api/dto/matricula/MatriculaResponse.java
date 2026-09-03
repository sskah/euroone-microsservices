package br.com.fiap.euroone_api.dto.matricula;

import java.time.LocalDate;

import br.com.fiap.euroone_api.dto.turma.TurmaResponse;
import br.com.fiap.euroone_api.dto.usuario.UsuarioResponse;
import lombok.Data;

@Data
public class MatriculaResponse {
    private Long id;
    private UsuarioResponse educando;
    private TurmaResponse turma;
    private LocalDate dataMatricula;
    private Integer progresso;
    private Integer pontos;
}
