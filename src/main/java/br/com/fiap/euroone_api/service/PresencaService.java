package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.dto.presenca.PresencaCreateRequest;
import br.com.fiap.euroone_api.dto.presenca.PresencaMapper;
import br.com.fiap.euroone_api.dto.presenca.PresencaUpdateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Matricula;
import br.com.fiap.euroone_api.model.Presenca;
import br.com.fiap.euroone_api.repository.PresencaRepository;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository repository;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private PresencaMapper mapper;

    public Presenca create(PresencaCreateRequest dto) {
        Matricula matricula = matriculaService.findById(dto.getMatriculaId());
        return repository.save(mapper.toModel(dto, matricula));
    }

    public Presenca update(Long id, PresencaUpdateRequest dto) {
        Presenca existente = findById(id);
        if (dto.getPresente() != null) existente.setPresente(dto.getPresente());
        if (dto.getObservacao() != null) existente.setObservacao(dto.getObservacao());
        return repository.save(existente);
    }

    public List<Presenca> findAll() {
        return repository.findAll();
    }

    public Presenca findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presença", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Presença", id);
        }
        repository.deleteById(id);
    }
}
