package br.com.fiap.euroone_api.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vínculo de um educando com uma turma.
 * Um mesmo educando não pode ter duas matrículas ativas para a mesma turma.
 */
@Data
@Entity
@NoArgsConstructor
@Table(
    name = "matriculas",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_matricula_educando_turma",
        columnNames = { "educando_id", "turma_id" }
    )
)
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Usuario educando;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(nullable = false)
    private LocalDate dataMatricula;

    /**
     * Progresso do educando na turma, de 0 a 100.
     */
    @Column(nullable = false)
    private Integer progresso = 0;

    /**
     * Pontos acumulados na gamificação.
     */
    @Column(nullable = false)
    private Integer pontos = 0;
}
