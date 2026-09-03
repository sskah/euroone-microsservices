package br.com.fiap.euroone_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.euroone_api.dto.resgate.ResgateCreateRequest;
import br.com.fiap.euroone_api.dto.resgate.ResgateUpdateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Matricula;
import br.com.fiap.euroone_api.model.Recompensa;
import br.com.fiap.euroone_api.model.Resgate;
import br.com.fiap.euroone_api.model.enums.StatusResgate;
import br.com.fiap.euroone_api.repository.ResgateRepository;

/**
 * Camada de negócio dos resgates de recompensas por pontos.
 *
 * Regras aplicadas:
 *  - Ao criar um resgate: valida pontos disponíveis, valida estoque,
 *    debita pontos da matrícula e decrementa o estoque da recompensa.
 *  - Ao mudar o status para CANCELADO: devolve pontos e estoque.
 *  - Não é possível reabrir um resgate cancelado.
 */
@Service
public class ResgateService {

    @Autowired
    private ResgateRepository repository;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private RecompensaService recompensaService;

    @Transactional
    public Resgate solicitar(ResgateCreateRequest dto) {
        Matricula matricula = matriculaService.findById(dto.getMatriculaId());
        Recompensa recompensa = recompensaService.findById(dto.getRecompensaId());

        if (Boolean.FALSE.equals(recompensa.getDisponivel())) {
            throw new IllegalArgumentException(
                "A recompensa '" + recompensa.getNome() + "' não está disponível para resgate.");
        }
        if (recompensa.getEstoque() <= 0) {
            throw new IllegalArgumentException(
                "Estoque esgotado para a recompensa '" + recompensa.getNome() + "'.");
        }
        if (matricula.getPontos() < recompensa.getCustoPontos()) {
            throw new IllegalArgumentException(String.format(
                "Pontos insuficientes: a matrícula tem %d pontos e a recompensa custa %d.",
                matricula.getPontos(), recompensa.getCustoPontos()));
        }

        // Debita pontos e decrementa estoque
        matricula.setPontos(matricula.getPontos() - recompensa.getCustoPontos());
        recompensa.setEstoque(recompensa.getEstoque() - 1);

        Resgate resgate = new Resgate();
        resgate.setMatricula(matricula);
        resgate.setRecompensa(recompensa);
        resgate.setStatus(StatusResgate.SOLICITADO);
        resgate.setDataResgate(LocalDateTime.now());
        resgate.setPontosGastos(recompensa.getCustoPontos());

        return repository.save(resgate);
    }

    @Transactional
    public Resgate atualizarStatus(Long id, ResgateUpdateRequest dto) {
        Resgate resgate = findById(id);
        StatusResgate novo = dto.getStatus();
        StatusResgate atual = resgate.getStatus();

        if (atual == StatusResgate.CANCELADO) {
            throw new IllegalArgumentException("Um resgate cancelado não pode ser alterado.");
        }
        if (atual == StatusResgate.ENTREGUE && novo != StatusResgate.ENTREGUE) {
            throw new IllegalArgumentException("Um resgate já entregue não pode voltar a outro status.");
        }

        // Se estamos cancelando, devolve pontos e estoque
        if (novo == StatusResgate.CANCELADO) {
            Matricula m = resgate.getMatricula();
            Recompensa r = resgate.getRecompensa();
            m.setPontos(m.getPontos() + resgate.getPontosGastos());
            r.setEstoque(r.getEstoque() + 1);
        }

        resgate.setStatus(novo);
        return repository.save(resgate);
    }

    public List<Resgate> findAll(Long matriculaId) {
        if (matriculaId == null) {
            return repository.findAll();
        }
        return repository.findByMatriculaIdOrderByDataResgateDesc(matriculaId);
    }

    public Resgate findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resgate", id));
    }

    @Transactional
    public void deleteById(Long id) {
        Resgate resgate = findById(id);
        // Se ainda não foi entregue, devolve pontos e estoque antes de apagar
        if (resgate.getStatus() != StatusResgate.ENTREGUE
                && resgate.getStatus() != StatusResgate.CANCELADO) {
            Matricula m = resgate.getMatricula();
            Recompensa r = resgate.getRecompensa();
            m.setPontos(m.getPontos() + resgate.getPontosGastos());
            r.setEstoque(r.getEstoque() + 1);
        }
        repository.deleteById(id);
    }
}
