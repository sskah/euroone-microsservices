package br.com.fiap.euroone_api.model;

import java.time.LocalDateTime;

import br.com.fiap.euroone_api.model.enums.TipoComunicado;
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
 * Comunicado interno trocado entre usuários da plataforma
 * (ex.: Gestão notificando Educador sobre queda de engajamento).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "comunicados")
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @Column(length = 120, nullable = false)
    private String assunto;

    @Column(length = 1000, nullable = false)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TipoComunicado tipo;

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    @Column(nullable = false)
    private Boolean lido = false;
}
