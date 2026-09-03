package br.com.fiap.euroone_api.dto.turma;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Curso;
import br.com.fiap.euroone_api.model.Turma;
import br.com.fiap.euroone_api.model.Usuario;

@Component
public class TurmaMapper {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Cria uma nova turma a partir do DTO e dos objetos já resolvidos
     * (Curso e Educador vêm carregados do banco pelo service).
     */
    public Turma toModel(TurmaCreateRequest dto, Curso curso, Usuario educador) {
        Turma turma = new Turma();
        turma.setCodigo(dto.getCodigo());
        turma.setPeriodo(dto.getPeriodo());
        turma.setSala(dto.getSala());
        turma.setCurso(curso);
        turma.setEducador(educador);
        return turma;
    }

    public Turma toModel(Long id, TurmaUpdateRequest dto, Curso curso, Usuario educador) {
        Turma turma = new Turma();
        turma.setId(id);
        turma.setCodigo(dto.getCodigo());
        turma.setPeriodo(dto.getPeriodo());
        turma.setSala(dto.getSala());
        turma.setCurso(curso);
        turma.setEducador(educador);
        return turma;
    }

    public TurmaResponse toDto(Turma entity) {
        return modelMapper.map(entity, TurmaResponse.class);
    }
}
