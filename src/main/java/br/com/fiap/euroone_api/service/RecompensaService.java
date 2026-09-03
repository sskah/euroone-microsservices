package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Recompensa;
import br.com.fiap.euroone_api.repository.RecompensaRepository;

@Service
public class RecompensaService {

    @Autowired
    private RecompensaRepository repository;

    public Recompensa create(Recompensa recompensa) {
        return repository.save(recompensa);
    }

    public Recompensa update(Long id, Recompensa recompensa) {
        Recompensa existente = findById(id);
        if (recompensa.getNome() != null) existente.setNome(recompensa.getNome());
        if (recompensa.getDescricao() != null) existente.setDescricao(recompensa.getDescricao());
        if (recompensa.getCustoPontos() != null) existente.setCustoPontos(recompensa.getCustoPontos());
        if (recompensa.getEstoque() != null) existente.setEstoque(recompensa.getEstoque());
        if (recompensa.getDisponivel() != null) existente.setDisponivel(recompensa.getDisponivel());
        return repository.save(existente);
    }

    public List<Recompensa> findAll() {
        return repository.findAll();
    }

    public Recompensa findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recompensa", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recompensa", id);
        }
        repository.deleteById(id);
    }
}
