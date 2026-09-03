package br.com.fiap.euroone_api.dto.resgate;

import java.time.LocalDateTime;

import br.com.fiap.euroone_api.dto.matricula.MatriculaResponse;
import br.com.fiap.euroone_api.dto.recompensa.RecompensaResponse;
import br.com.fiap.euroone_api.model.enums.StatusResgate;
import lombok.Data;

@Data
public class ResgateResponse {
    private Long id;
    private MatriculaResponse matricula;
    private RecompensaResponse recompensa;
    private StatusResgate status;
    private LocalDateTime dataResgate;
    private Integer pontosGastos;
}
