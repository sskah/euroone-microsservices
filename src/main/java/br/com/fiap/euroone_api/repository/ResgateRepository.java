package br.com.fiap.euroone_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.euroone_api.model.Resgate;

@Repository
public interface ResgateRepository extends JpaRepository<Resgate, Long> {

    List<Resgate> findByMatriculaIdOrderByDataResgateDesc(Long matriculaId);
}
