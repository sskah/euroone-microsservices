package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Curso;
import br.com.fiap.euroone_api.repository.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;

    public Curso create(Curso curso) {
        return repository.save(curso);
    }

    public Curso update(Long id, Curso curso) {
        Curso existente = findById(id);
        if (curso.getNome() != null) existente.setNome(curso.getNome());
        if (curso.getTrilha() != null) existente.setTrilha(curso.getTrilha());
        if (curso.getCargaHoraria() != null) existente.setCargaHoraria(curso.getCargaHoraria());
        if (curso.getDescricao() != null) existente.setDescricao(curso.getDescricao());
        return repository.save(existente);
    }

    public List<Curso> findAll() {
        return repository.findAll();
    }

    public Curso findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Curso", id);
        }
        repository.deleteById(id);
    }
}
