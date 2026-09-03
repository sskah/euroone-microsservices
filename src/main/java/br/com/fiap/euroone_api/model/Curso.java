package br.com.fiap.euroone_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Curso oferecido pela plataforma
 * (ex.: Farmacovigilância, Bioequivalência, Epidemiologia, P&D Farmacêutico).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String nome;

    /**
     * Trilha de aprendizagem (ex.: Regulatório, Pesquisa, Compliance).
     */
    @Column(length = 60)
    private String trilha;

    /**
     * Carga horária total do curso, em horas.
     */
    @Column(nullable = false)
    private Integer cargaHoraria;

    @Column(length = 500)
    private String descricao;
}
