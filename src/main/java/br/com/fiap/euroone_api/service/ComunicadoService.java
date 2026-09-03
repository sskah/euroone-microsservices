package br.com.fiap.euroone_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.euroone_api.dto.comunicado.ComunicadoCreateRequest;
import br.com.fiap.euroone_api.dto.comunicado.ComunicadoMapper;
import br.com.fiap.euroone_api.dto.comunicado.ComunicadoUpdateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.Comunicado;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.repository.ComunicadoRepository;

@Service
public class ComunicadoService {

    @Autowired
    private ComunicadoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ComunicadoMapper mapper;

    public Comunicado create(ComunicadoCreateRequest dto) {
        Usuario remetente = usuarioService.findById(dto.getRemetenteId());
        Usuario destinatario = usuarioService.findById(dto.getDestinatarioId());
        if (remetente.getId().equals(destinatario.getId())) {
            throw new IllegalArgumentException("Remetente e destinatário não podem ser o mesmo usuário.");
        }
        return repository.save(mapper.toModel(dto, remetente, destinatario));
    }

    public Comunicado update(Long id, ComunicadoUpdateRequest dto) {
        Comunicado existente = findById(id);
        if (dto.getAssunto() != null) existente.setAssunto(dto.getAssunto());
        if (dto.getMensagem() != null) existente.setMensagem(dto.getMensagem());
        if (dto.getTipo() != null) existente.setTipo(dto.getTipo());
        if (dto.getLido() != null) existente.setLido(dto.getLido());
        return repository.save(existente);
    }

    public List<Comunicado> findAll() {
        return repository.findAll();
    }

    public Comunicado findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunicado", id));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Comunicado", id);
        }
        repository.deleteById(id);
    }
}
