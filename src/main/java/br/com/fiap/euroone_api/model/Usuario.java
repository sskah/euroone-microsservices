package br.com.fiap.euroone_api.model;

import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa qualquer usuário da plataforma EuroOne (educando, educador ou gestão).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 120, nullable = false, unique = true)
    private String email;

    /**
     * Matrícula / código do crachá (ex: EF-2024-1138, GE-1029, PF-9821).
     */
    @Column(length = 30, nullable = false, unique = true)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PerfilUsuario perfil;

    /**
     * Campus / unidade Eurofarma à qual o usuário está vinculado.
     */
    @Column(length = 60)
    private String campus;
}
