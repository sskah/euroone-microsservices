package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.dto.missao.MissaoCreateRequest;
import br.com.fiap.euroone_api.dto.missao.MissaoMapper;
import br.com.fiap.euroone_api.dto.missao.MissaoUpdateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Missao;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import br.com.fiap.euroone_api.repository.MissaoRepository;

@Service
public class MissaoService {

    @Autowired
    private MissaoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MissaoMapper mapper;

    public Missao create(MissaoCreateRequest dto) {
        Usuario educando = usuarioService.findById(dto.getEducandoId());
        if (educando.getPerfil() != PerfilUsuario.EDUCANDO) {
            throw new IllegalArgumentException(
                "Missões só podem ser atribuídas a usuários com perfil EDUCANDO. Perfil atual: " + educando.getPerfil());
        }
        return repository.save(mapper.toModel(dto, educando));
    }

    public Missao update(Long id, MissaoUpdateRequest dto) {
        Missao existente = findById(id);
        if (dto.getTitulo() != null) existente.setTitulo(dto.getTitulo());
        if (dto.getDescricao() != null) existente.setDescricao(dto.getDescricao());
        if (dto.getPontos() != null) existente.setPontos(dto.getPontos());
        if (dto.getStatus() != null) existente.setStatus(dto.getStatus());
        if (dto.getPrazo() != null) existente.setPrazo(dto.getPrazo());
        return repository.save(existente);
    }

    public List<Missao> findAll() {
        return repository.findAll();
    }

    public Missao findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missão", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Missão", id);
        }
        repository.deleteById(id);
    }
}
