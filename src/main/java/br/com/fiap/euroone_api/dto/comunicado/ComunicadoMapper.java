package br.com.fiap.euroone_api.dto.comunicado;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Comunicado;
import br.com.fiap.euroone_api.model.Usuario;

@Component
public class ComunicadoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Comunicado toModel(ComunicadoCreateRequest dto, Usuario remetente, Usuario destinatario) {
        Comunicado comunicado = new Comunicado();
        comunicado.setRemetente(remetente);
        comunicado.setDestinatario(destinatario);
        comunicado.setAssunto(dto.getAssunto());
        comunicado.setMensagem(dto.getMensagem());
        comunicado.setTipo(dto.getTipo());
        comunicado.setDataEnvio(LocalDateTime.now());
        comunicado.setLido(false);
        return comunicado;
    }

    public ComunicadoResponse toDto(Comunicado entity) {
        return modelMapper.map(entity, ComunicadoResponse.class);
    }
}
