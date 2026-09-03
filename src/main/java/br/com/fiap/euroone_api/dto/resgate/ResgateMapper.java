package br.com.fiap.euroone_api.dto.resgate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Resgate;

@Component
public class ResgateMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ResgateResponse toDto(Resgate entity) {
        return modelMapper.map(entity, ResgateResponse.class);
    }
}
