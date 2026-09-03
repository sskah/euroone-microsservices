package br.com.fiap.euroone_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.euroone_api.model.ConversaEuri;

@Repository
public interface ConversaEuriRepository extends JpaRepository<ConversaEuri, Long> {

    List<ConversaEuri> findByUsuarioIdOrderByIniciadaEmDesc(Long usuarioId);
}
