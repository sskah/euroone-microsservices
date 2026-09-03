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
 * Item do catálogo de recompensas resgatáveis com pontos
 * (ex.: Vale-livro, Day-off, Mentoria 1:1).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "recompensas")
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String nome;

    @Column(length = 500)
    private String descricao;

    /**
     * Custo em pontos para resgatar a recompensa.
     */
    @Column(nullable = false)
    private Integer custoPontos;

    /**
     * Quantidade disponível em estoque.
     */
    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private Boolean disponivel = true;
}
