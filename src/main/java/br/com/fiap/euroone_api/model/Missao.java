package br.com.fiap.euroone_api.model;

import java.time.LocalDate;

import br.com.fiap.euroone_api.model.enums.StatusMissao;
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
 * Missão gamificada atribuída a um educando (leitura de material, quiz, fórum, etc.).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "missoes")
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    /**
     * Pontos que o educando recebe ao concluir a missão.
     */
    @Column(nullable = false)
    private Integer pontos;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusMissao status = StatusMissao.PENDENTE;

    @Column(nullable = false)
    private LocalDate prazo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Usuario educando;
}
