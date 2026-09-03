package br.com.fiap.euroone_api.dto.usuario;

import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateRequest {

    @Size(min = 3, max = 100)
    private String nome;

    @Email(message = "Email inválido")
    @Size(max = 120)
    private String email;

    @Size(min = 3, max = 30)
    private String matricula;

    private PerfilUsuario perfil;

    @Size(max = 60)
    private String campus;
}
