package br.com.fiap.euroone_api.dto.recompensa;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Recompensa;

@Component
public class RecompensaMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Recompensa toModel(RecompensaCreateRequest dto) {
        Recompensa recompensa = modelMapper.map(dto, Recompensa.class);
        if (recompensa.getDisponivel() == null) {
            recompensa.setDisponivel(true);
        }
        return recompensa;
    }

    public Recompensa toModel(Long id, RecompensaUpdateRequest dto) {
        Recompensa recompensa = modelMapper.map(dto, Recompensa.class);
        recompensa.setId(id);
        return recompensa;
    }

    public RecompensaResponse toDto(Recompensa entity) {
        return modelMapper.map(entity, RecompensaResponse.class);
    }
}
