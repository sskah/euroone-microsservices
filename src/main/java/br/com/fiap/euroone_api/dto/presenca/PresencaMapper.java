package br.com.fiap.euroone_api.dto.presenca;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Matricula;
import br.com.fiap.euroone_api.model.Presenca;

@Component
public class PresencaMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Presenca toModel(PresencaCreateRequest dto, Matricula matricula) {
        Presenca presenca = new Presenca();
        presenca.setMatricula(matricula);
        presenca.setData(dto.getData());
        presenca.setPresente(dto.getPresente());
        presenca.setObservacao(dto.getObservacao());
        return presenca;
    }

    public PresencaResponse toDto(Presenca entity) {
        return modelMapper.map(entity, PresencaResponse.class);
    }
}
