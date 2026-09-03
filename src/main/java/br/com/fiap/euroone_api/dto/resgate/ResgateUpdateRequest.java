package br.com.fiap.euroone_api.dto.resgate;

import br.com.fiap.euroone_api.model.enums.StatusResgate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResgateUpdateRequest {

    @NotNull(message = "O novo status é obrigatório")
    private StatusResgate status;
}
