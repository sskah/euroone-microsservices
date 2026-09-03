package br.com.fiap.euroone_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.euroone_api.model.MensagemEuri;

@Repository
public interface MensagemEuriRepository extends JpaRepository<MensagemEuri, Long> {

    List<MensagemEuri> findByConversaIdOrderByEnviadaEmAsc(Long conversaId);

    void deleteByConversaId(Long conversaId);
}
