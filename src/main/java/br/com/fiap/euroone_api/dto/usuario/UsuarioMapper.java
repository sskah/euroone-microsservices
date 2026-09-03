package br.com.fiap.euroone_api.dto.usuario;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.euroone_api.model.Usuario;

@Component
public class UsuarioMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toModel(UsuarioCreateRequest dto) {
        return modelMapper.map(dto, Usuario.class);
    }

    public Usuario toModel(Long id, UsuarioUpdateRequest dto) {
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuario.setId(id);
        return usuario;
    }

    public UsuarioResponse toDto(Usuario entity) {
        return modelMapper.map(entity, UsuarioResponse.class);
    }
}
