package br.com.fiap.euroone_api.dto.usuario;

import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private PerfilUsuario perfil;
    private String campus;
}
