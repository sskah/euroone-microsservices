package br.com.fiap.euroone_api.model;

import java.time.LocalDateTime;

import br.com.fiap.euroone_api.model.enums.RemetenteEuri;
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
 * Mensagem individual dentro de uma {@link ConversaEuri}.
 * Cada mensagem tem um {@link RemetenteEuri} (USUARIO ou EURI).
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "mensagens_euri")
public class MensagemEuri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "conversa_id", nullable = false)
    private ConversaEuri conversa;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private RemetenteEuri remetente;

    @Column(length = 2000, nullable = false)
    private String conteudo;

    @Column(nullable = false)
    private LocalDateTime enviadaEm;
}
