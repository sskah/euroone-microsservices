package br.com.fiap.euroone_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPerfil(PerfilUsuario perfil);

    boolean existsByEmail(String email);

    boolean existsByMatricula(String matricula);
}
