package br.com.fiap.euroone_api.dto.usuario;

import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 120)
    private String email;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(min = 3, max = 30, message = "A matrícula deve ter entre 3 e 30 caracteres")
    private String matricula;

    @NotNull(message = "O perfil é obrigatório (EDUCANDO, EDUCADOR ou GESTAO)")
    private PerfilUsuario perfil;

    @Size(max = 60)
    private String campus;
}
