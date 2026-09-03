package br.com.fiap.euroone_api.dto.matricula;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Matricula;
import br.com.fiap.euroone_api.model.Turma;
import br.com.fiap.euroone_api.model.Usuario;

@Component
public class MatriculaMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Matricula toModel(MatriculaCreateRequest dto, Usuario educando, Turma turma) {
        Matricula matricula = new Matricula();
        matricula.setEducando(educando);
        matricula.setTurma(turma);
        matricula.setDataMatricula(dto.getDataMatricula());
        matricula.setProgresso(dto.getProgresso() == null ? 0 : dto.getProgresso());
        matricula.setPontos(dto.getPontos() == null ? 0 : dto.getPontos());
        return matricula;
    }

    public MatriculaResponse toDto(Matricula entity) {
        return modelMapper.map(entity, MatriculaResponse.class);
    }
}
