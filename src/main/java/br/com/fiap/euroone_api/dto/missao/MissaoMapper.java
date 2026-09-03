package br.com.fiap.euroone_api.dto.missao;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Missao;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.StatusMissao;

@Component
public class MissaoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Missao toModel(MissaoCreateRequest dto, Usuario educando) {
        Missao missao = new Missao();
        missao.setTitulo(dto.getTitulo());
        missao.setDescricao(dto.getDescricao());
        missao.setPontos(dto.getPontos());
        missao.setStatus(dto.getStatus() == null ? StatusMissao.PENDENTE : dto.getStatus());
        missao.setPrazo(dto.getPrazo());
        missao.setEducando(educando);
        return missao;
    }

    public MissaoResponse toDto(Missao entity) {
        return modelMapper.map(entity, MissaoResponse.class);
    }
}
