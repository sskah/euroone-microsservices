package br.com.fiap.euroone_api.model.enums;

/**
 * Perfis de usuário do EuroOne, conforme definido nas Sprints 1 e 2.
 *
 *  - EDUCANDO : aluno da plataforma (acompanha pontos, missões e presença).
 *  - EDUCADOR : professor (monitora turmas e gera intervenções).
 *  - GESTAO   : gestão (visualiza indicadores consolidados).
 */
public enum PerfilUsuario {
    EDUCANDO,
    EDUCADOR,
    GESTAO
}
