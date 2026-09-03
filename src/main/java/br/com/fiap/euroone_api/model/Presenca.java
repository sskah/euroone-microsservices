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
 * Registro de presença ou falta de um educando em uma data específica.
 * Cada matrícula tem no máximo um registro por data.
 */
@Data
@Entity
@NoArgsConstructor
@Table(
    name = "presencas",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_presenca_matricula_data",
        columnNames = { "matricula_id", "data" }
    )
)
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Boolean presente;

    /**
     * Observação opcional lançada pelo educador (ex.: "chegou atrasado", "atestado").
     */
    @Column(length = 250)
    private String observacao;
}
