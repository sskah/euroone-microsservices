package br.com.fiap.euroone_api.model.enums;

/**
 * Ciclo de vida de um resgate de recompensa por um educando.
 *  - SOLICITADO : o educando pediu o resgate; pontos já foram descontados
 *                 e o item foi reservado no estoque.
 *  - APROVADO   : a gestão validou o pedido.
 *  - ENTREGUE   : o item foi entregue ao educando.
 *  - CANCELADO  : o pedido foi cancelado; pontos são devolvidos e o item
 *                 retorna ao estoque.
 */
public enum StatusResgate {
    SOLICITADO,
    APROVADO,
    ENTREGUE,
    CANCELADO
}
