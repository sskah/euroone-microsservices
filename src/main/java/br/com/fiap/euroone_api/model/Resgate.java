package br.com.fiap.euroone_api.model;

import java.time.LocalDateTime;

import br.com.fiap.euroone_api.model.enums.StatusResgate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resgate de uma {@link Recompensa} feito por um educando através de sua
 * {@link Matricula}. Ao criar um resgate, o service desconta os pontos da
 * matrícula e decrementa o estoque da recompensa. Se o status mudar para
 * CANCELADO, os pontos e o estoque são devolvidos.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "resgates")
public class Resgate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recompensa_id", nullable = false)
    private Recompensa recompensa;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusResgate status = StatusResgate.SOLICITADO;

    @Column(nullable = false)
    private LocalDateTime dataResgate;

    /**
     * Fotografa o custo em pontos no momento do resgate. Mesmo que a
     * recompensa mude de preço depois, o histórico do resgate preserva
     * o valor original cobrado do educando.
     */
    @Column(nullable = false)
    private Integer pontosGastos;
}
