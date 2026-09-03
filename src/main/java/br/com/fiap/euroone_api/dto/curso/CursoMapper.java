package br.com.fiap.euroone_api.dto.curso;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Curso;

@Component
public class CursoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Curso toModel(CursoCreateRequest dto) {
        return modelMapper.map(dto, Curso.class);
    }

    public Curso toModel(Long id, CursoUpdateRequest dto) {
        Curso curso = modelMapper.map(dto, Curso.class);
        curso.setId(id);
        return curso;
    }

    public CursoResponse toDto(Curso entity) {
        return modelMapper.map(entity, CursoResponse.class);
    }
}
