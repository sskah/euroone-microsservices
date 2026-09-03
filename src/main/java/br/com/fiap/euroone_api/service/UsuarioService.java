package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import br.com.fiap.euroone_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario create(Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com o email: " + usuario.getEmail());
        }
        if (repository.existsByMatricula(usuario.getMatricula())) {
            throw new IllegalArgumentException("Já existe um usuário com a matrícula: " + usuario.getMatricula());
        }
        return repository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario existente = findById(id);
        // preserva id da entidade persistente e sobrescreve apenas campos fornecidos
        if (usuario.getNome() != null) existente.setNome(usuario.getNome());
        if (usuario.getEmail() != null) existente.setEmail(usuario.getEmail());
        if (usuario.getMatricula() != null) existente.setMatricula(usuario.getMatricula());
        if (usuario.getPerfil() != null) existente.setPerfil(usuario.getPerfil());
        if (usuario.getCampus() != null) existente.setCampus(usuario.getCampus());
        return repository.save(existente);
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public List<Usuario> findByPerfil(PerfilUsuario perfil) {
        return repository.findByPerfil(perfil);
    }

    public Usuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário", id);
        }
        repository.deleteById(id);
    }
}
