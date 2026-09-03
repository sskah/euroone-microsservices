package br.com.fiap.euroone_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.euroone_api.model.Recompensa;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
}
