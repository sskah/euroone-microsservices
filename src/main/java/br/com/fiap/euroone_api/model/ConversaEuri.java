package br.com.fiap.euroone_api.model;

import java.time.LocalDateTime;

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
 * Sessão de conversa entre um usuário da plataforma e a assistente Euri
 * (mascote / chatbot do EuroOne).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "conversas_euri")
public class ConversaEuri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 120, nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDateTime iniciadaEm;

    /**
     * Indica se a conversa ainda está aberta. Conversas encerradas
     * permanecem no histórico mas não recebem novas mensagens.
     */
    @Column(nullable = false)
    private Boolean ativa = true;
}
