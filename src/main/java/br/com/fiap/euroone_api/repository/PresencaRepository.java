package br.com.fiap.euroone_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.euroone_api.model.Presenca;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
}
