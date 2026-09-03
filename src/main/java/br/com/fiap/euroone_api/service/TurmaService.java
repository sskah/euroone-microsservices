package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.dto.turma.TurmaCreateRequest;
import br.com.fiap.euroone_api.dto.turma.TurmaMapper;
import br.com.fiap.euroone_api.dto.turma.TurmaUpdateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Curso;
import br.com.fiap.euroone_api.model.Turma;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import br.com.fiap.euroone_api.repository.TurmaRepository;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository repository;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TurmaMapper mapper;

    public Turma create(TurmaCreateRequest dto) {
        Curso curso = cursoService.findById(dto.getCursoId());
        Usuario educador = null;
        if (dto.getEducadorId() != null) {
            educador = usuarioService.findById(dto.getEducadorId());
            validarPerfilEducador(educador);
        }
        return repository.save(mapper.toModel(dto, curso, educador));
    }

    public Turma update(Long id, TurmaUpdateRequest dto) {
        Turma existente = findById(id);

        if (dto.getCodigo() != null) existente.setCodigo(dto.getCodigo());
        if (dto.getPeriodo() != null) existente.setPeriodo(dto.getPeriodo());
        if (dto.getSala() != null) existente.setSala(dto.getSala());

        if (dto.getCursoId() != null) {
            existente.setCurso(cursoService.findById(dto.getCursoId()));
        }
        if (dto.getEducadorId() != null) {
            Usuario educador = usuarioService.findById(dto.getEducadorId());
            validarPerfilEducador(educador);
            existente.setEducador(educador);
        }
        return repository.save(existente);
    }

    public List<Turma> findAll() {
        return repository.findAll();
    }

    public Turma findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Turma", id);
        }
        repository.deleteById(id);
    }

    private void validarPerfilEducador(Usuario usuario) {
        if (usuario.getPerfil() != PerfilUsuario.EDUCADOR) {
            throw new IllegalArgumentException(
                "O usuário informado como educador deve ter perfil EDUCADOR. Perfil atual: " + usuario.getPerfil());
        }
    }
}
