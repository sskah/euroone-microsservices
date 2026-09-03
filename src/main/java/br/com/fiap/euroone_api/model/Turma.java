package br.com.fiap.euroone_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Turma de um curso (ex.: Turma 24-B Noturno de Farmacovigilância).
 * Cada turma pertence a um {@link Curso} e possui um {@link Usuario} com
 * perfil EDUCADOR como responsável.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60, nullable = false)
    private String codigo;

    @Column(length = 60)
    private String periodo;

    @Column(length = 30)
    private String sala;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "educador_id")
    private Usuario educador;
}
