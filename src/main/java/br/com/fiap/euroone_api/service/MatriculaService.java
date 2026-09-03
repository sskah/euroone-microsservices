package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.dto.matricula.MatriculaCreateRequest;
import br.com.fiap.euroone_api.dto.matricula.MatriculaMapper;
import br.com.fiap.euroone_api.dto.matricula.MatriculaUpdateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Matricula;
import br.com.fiap.euroone_api.model.Turma;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import br.com.fiap.euroone_api.repository.MatriculaRepository;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private MatriculaMapper mapper;

    public Matricula create(MatriculaCreateRequest dto) {
        Usuario educando = usuarioService.findById(dto.getEducandoId());
        if (educando.getPerfil() != PerfilUsuario.EDUCANDO) {
            throw new IllegalArgumentException(
                "Somente usuários com perfil EDUCANDO podem ser matriculados. Perfil atual: " + educando.getPerfil());
        }
        Turma turma = turmaService.findById(dto.getTurmaId());
        return repository.save(mapper.toModel(dto, educando, turma));
    }

    public Matricula update(Long id, MatriculaUpdateRequest dto) {
        Matricula existente = findById(id);
        if (dto.getProgresso() != null) existente.setProgresso(dto.getProgresso());
        if (dto.getPontos() != null) existente.setPontos(dto.getPontos());
        return repository.save(existente);
    }

    public List<Matricula> findAll() {
        return repository.findAll();
    }

    public Matricula findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Matrícula", id);
        }
        repository.deleteById(id);
    }
}
