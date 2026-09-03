package br.com.fiap.euroone_api.dto.euri;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.ConversaEuri;
import br.com.fiap.euroone_api.model.MensagemEuri;

@Component
public class EuriMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ConversaEuriResponse toDto(ConversaEuri entity) {
        // Sem carregar mensagens (versão "leve" para listagens).
        return modelMapper.map(entity, ConversaEuriResponse.class);
    }

    public ConversaEuriResponse toDtoComMensagens(ConversaEuri entity, List<MensagemEuri> mensagens) {
        ConversaEuriResponse dto = modelMapper.map(entity, ConversaEuriResponse.class);
        dto.setMensagens(mensagens.stream().map(this::toDto).toList());
        return dto;
    }

    public MensagemEuriResponse toDto(MensagemEuri entity) {
        return modelMapper.map(entity, MensagemEuriResponse.class);
    }
}
