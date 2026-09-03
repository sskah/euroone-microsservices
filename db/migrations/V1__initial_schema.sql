-- =====================================================================
-- EuroOne API - V1__initial_schema.sql
-- Schema inicial do banco de dados MySQL para o projeto EuroOne (Sprint 3).
--
-- Este script:
--  1. Cria (se necessário) o banco `euroone`.
--  2. Cria as 11 tabelas do domínio (equivalentes às entidades JPA).
--  3. Popula dados iniciais para facilitar testes via DBeaver e Swagger.
--
-- Como executar manualmente no DBeaver:
--   - Conecte no MySQL (localhost:3306, user: root, senha: root_pwd)
--   - Abra este arquivo (SQL Editor) e execute todo o script (Alt+X).
--
-- Ao subir o container do MySQL montando este arquivo em
-- /docker-entrypoint-initdb.d, ele é executado automaticamente na primeira
-- inicialização do container.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS euroone
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE euroone;

-- ---------------------------------------------------------------------
-- Limpeza (idempotência) - remove tabelas em ordem inversa de dependência.
-- ---------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS mensagens_euri;
DROP TABLE IF EXISTS conversas_euri;
DROP TABLE IF EXISTS resgates;
DROP TABLE IF EXISTS comunicados;
DROP TABLE IF EXISTS presencas;
DROP TABLE IF EXISTS missoes;
DROP TABLE IF EXISTS matriculas;
DROP TABLE IF EXISTS turmas;
DROP TABLE IF EXISTS recompensas;
DROP TABLE IF EXISTS cursos;
DROP TABLE IF EXISTS usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- Tabela: usuarios
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(120) NOT NULL,
    matricula   VARCHAR(30)  NOT NULL,
    perfil      VARCHAR(20)  NOT NULL,
    campus      VARCHAR(60)  NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email     UNIQUE (email),
    CONSTRAINT uk_usuarios_matricula UNIQUE (matricula),
    CONSTRAINT ck_usuarios_perfil CHECK (perfil IN ('EDUCANDO','EDUCADOR','GESTAO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: cursos
-- ---------------------------------------------------------------------
CREATE TABLE cursos (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    nome           VARCHAR(100) NOT NULL,
    trilha         VARCHAR(60)  NULL,
    carga_horaria  INT          NOT NULL,
    descricao      VARCHAR(500) NULL,
    CONSTRAINT pk_cursos PRIMARY KEY (id),
    CONSTRAINT uk_cursos_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: turmas
-- ---------------------------------------------------------------------
CREATE TABLE turmas (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    codigo       VARCHAR(60) NOT NULL,
    periodo      VARCHAR(60) NULL,
    sala         VARCHAR(30) NULL,
    curso_id     BIGINT      NOT NULL,
    educador_id  BIGINT      NULL,
    CONSTRAINT pk_turmas PRIMARY KEY (id),
    CONSTRAINT fk_turmas_curso    FOREIGN KEY (curso_id)    REFERENCES cursos(id),
    CONSTRAINT fk_turmas_educador FOREIGN KEY (educador_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: matriculas
-- ---------------------------------------------------------------------
CREATE TABLE matriculas (
    id              BIGINT NOT NULL AUTO_INCREMENT,
    educando_id     BIGINT NOT NULL,
    turma_id        BIGINT NOT NULL,
    data_matricula  DATE   NOT NULL,
    progresso       INT    NOT NULL DEFAULT 0,
    pontos          INT    NOT NULL DEFAULT 0,
    CONSTRAINT pk_matriculas PRIMARY KEY (id),
    CONSTRAINT fk_matriculas_educando FOREIGN KEY (educando_id) REFERENCES usuarios(id),
    CONSTRAINT fk_matriculas_turma    FOREIGN KEY (turma_id)    REFERENCES turmas(id),
    CONSTRAINT uk_matricula_educando_turma UNIQUE (educando_id, turma_id),
    CONSTRAINT ck_matriculas_progresso CHECK (progresso BETWEEN 0 AND 100),
    CONSTRAINT ck_matriculas_pontos    CHECK (pontos    >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: missoes
-- ---------------------------------------------------------------------
CREATE TABLE missoes (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    titulo        VARCHAR(100) NOT NULL,
    descricao     VARCHAR(500) NULL,
    pontos        INT          NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE',
    prazo         DATE         NOT NULL,
    educando_id   BIGINT       NOT NULL,
    CONSTRAINT pk_missoes PRIMARY KEY (id),
    CONSTRAINT fk_missoes_educando FOREIGN KEY (educando_id) REFERENCES usuarios(id),
    CONSTRAINT ck_missoes_status CHECK (status IN ('PENDENTE','EM_ANDAMENTO','CONCLUIDA')),
    CONSTRAINT ck_missoes_pontos CHECK (pontos > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: recompensas
-- ---------------------------------------------------------------------
CREATE TABLE recompensas (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    nome           VARCHAR(100) NOT NULL,
    descricao      VARCHAR(500) NULL,
    custo_pontos   INT          NOT NULL,
    estoque        INT          NOT NULL,
    disponivel     BIT(1)       NOT NULL DEFAULT b'1',
    CONSTRAINT pk_recompensas PRIMARY KEY (id),
    CONSTRAINT uk_recompensas_nome UNIQUE (nome),
    CONSTRAINT ck_recompensas_custo   CHECK (custo_pontos > 0),
    CONSTRAINT ck_recompensas_estoque CHECK (estoque      >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: presencas
-- ---------------------------------------------------------------------
CREATE TABLE presencas (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    matricula_id   BIGINT       NOT NULL,
    data           DATE         NOT NULL,
    presente       BIT(1)       NOT NULL,
    observacao     VARCHAR(250) NULL,
    CONSTRAINT pk_presencas PRIMARY KEY (id),
    CONSTRAINT fk_presencas_matricula FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
    CONSTRAINT uk_presenca_matricula_data UNIQUE (matricula_id, data)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: comunicados
-- ---------------------------------------------------------------------
CREATE TABLE comunicados (
    id                BIGINT        NOT NULL AUTO_INCREMENT,
    remetente_id      BIGINT        NOT NULL,
    destinatario_id   BIGINT        NOT NULL,
    assunto           VARCHAR(120)  NOT NULL,
    mensagem          VARCHAR(1000) NOT NULL,
    tipo              VARCHAR(20)   NOT NULL,
    data_envio        DATETIME(6)   NOT NULL,
    lido              BIT(1)        NOT NULL DEFAULT b'0',
    CONSTRAINT pk_comunicados PRIMARY KEY (id),
    CONSTRAINT fk_comunicados_remetente    FOREIGN KEY (remetente_id)    REFERENCES usuarios(id),
    CONSTRAINT fk_comunicados_destinatario FOREIGN KEY (destinatario_id) REFERENCES usuarios(id),
    CONSTRAINT ck_comunicados_tipo CHECK (tipo IN ('POSITIVO','ATENCAO','CRITICO')),
    CONSTRAINT ck_comunicados_diff_users CHECK (remetente_id <> destinatario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: resgates (educando gasta pontos para pegar uma recompensa)
-- ---------------------------------------------------------------------
CREATE TABLE resgates (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    matricula_id    BIGINT      NOT NULL,
    recompensa_id   BIGINT      NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'SOLICITADO',
    data_resgate    DATETIME(6) NOT NULL,
    pontos_gastos   INT         NOT NULL,
    CONSTRAINT pk_resgates PRIMARY KEY (id),
    CONSTRAINT fk_resgates_matricula  FOREIGN KEY (matricula_id)  REFERENCES matriculas(id),
    CONSTRAINT fk_resgates_recompensa FOREIGN KEY (recompensa_id) REFERENCES recompensas(id),
    CONSTRAINT ck_resgates_status CHECK (status IN ('SOLICITADO','APROVADO','ENTREGUE','CANCELADO')),
    CONSTRAINT ck_resgates_pontos CHECK (pontos_gastos > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: conversas_euri (sessão de chat com a assistente Euri)
-- ---------------------------------------------------------------------
CREATE TABLE conversas_euri (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    usuario_id   BIGINT       NOT NULL,
    titulo       VARCHAR(120) NOT NULL,
    iniciada_em  DATETIME(6)  NOT NULL,
    ativa        BIT(1)       NOT NULL DEFAULT b'1',
    CONSTRAINT pk_conversas_euri PRIMARY KEY (id),
    CONSTRAINT fk_conversas_euri_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Tabela: mensagens_euri (turnos individuais dentro de uma conversa)
-- ---------------------------------------------------------------------
CREATE TABLE mensagens_euri (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    conversa_id  BIGINT        NOT NULL,
    remetente    VARCHAR(10)   NOT NULL,
    conteudo     VARCHAR(2000) NOT NULL,
    enviada_em   DATETIME(6)   NOT NULL,
    CONSTRAINT pk_mensagens_euri PRIMARY KEY (id),
    CONSTRAINT fk_mensagens_euri_conversa FOREIGN KEY (conversa_id) REFERENCES conversas_euri(id),
    CONSTRAINT ck_mensagens_euri_remetente CHECK (remetente IN ('USUARIO','EURI'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================================
-- SEED DATA - dados de exemplo baseados no protótipo EuroOne
-- =====================================================================

-- Usuários -------------------------------------------------------------
INSERT INTO usuarios (nome, email, matricula, perfil, campus) VALUES
    ('Marina Soares',      'aluno@gmail.com',           'EF-2024-1138', 'EDUCANDO', 'Itapevi'),
    ('João Pedro Lima',    'joao.pedro@gmail.com',      'EF-2024-1142', 'EDUCANDO', 'Itapevi'),
    ('Beatriz Almeida',    'beatriz.almeida@gmail.com', 'EF-2024-1174', 'EDUCANDO', 'Ribeirão Preto'),
    ('Dr. Renato Lima',    'pf_9821@gmail.com',         'PF-9821',      'EDUCADOR', 'Itapevi'),
    ('Dra. Cláudia Mota',  'pf_102030@gmail.com',       'PF-102030',    'EDUCADOR', 'Ribeirão Preto'),
    ('Patrícia Andrade',   'eurone_1029@gmail.com',     'GE-1029',      'GESTAO',   'Sede SP'),
    ('Ricardo Fonseca',    'eurone_998877@gmail.com',   'GE-998877',    'GESTAO',   'Sede SP');

-- Cursos ---------------------------------------------------------------
INSERT INTO cursos (nome, trilha, carga_horaria, descricao) VALUES
    ('Especialização em Farmacovigilância', 'Regulatório', 120, 'Estudos de segurança e efeitos adversos.'),
    ('Boas práticas em pesquisa clínica',   'Pesquisa',     80, 'Protocolos e boas práticas em pesquisa clínica.'),
    ('Fundamentos de epidemiologia',        'Pesquisa',     60, 'Introdução aos métodos epidemiológicos.'),
    ('Bioequivalência',                     'P&D',          90, 'Fundamentos de estudos de bioequivalência.');

-- Turmas ---------------------------------------------------------------
INSERT INTO turmas (codigo, periodo, sala, curso_id, educador_id) VALUES
    ('24-B',  'Noturno',  'Auditório 2', 1, 4),
    ('24-C',  'Matutino', 'Sala 305',    1, 4),
    ('23-A',  'Noturno',  'Sala 210',    4, 5);

-- Matrículas -----------------------------------------------------------
INSERT INTO matriculas (educando_id, turma_id, data_matricula, progresso, pontos) VALUES
    (1, 1, '2024-02-12', 73, 4820),
    (2, 2, '2024-02-12', 41,  940),
    (3, 1, '2024-02-15', 96, 5210);

-- Missões --------------------------------------------------------------
INSERT INTO missoes (titulo, descricao, pontos, status, prazo, educando_id) VALUES
    ('Estudo de caso: Paciente 47', 'Analisar caso clínico completo.',            300, 'EM_ANDAMENTO', '2024-11-05', 1),
    ('Quiz Módulo 5',               'Responder ao quiz do módulo 5.',             150, 'PENDENTE',     '2024-11-08', 1),
    ('Fórum de casos clínicos',     'Participar do fórum obrigatório.',           200, 'PENDENTE',     '2024-11-10', 2),
    ('Revisão de bioequivalência',  'Revisar os capítulos 1 a 4.',                250, 'CONCLUIDA',    '2024-10-20', 3);

-- Recompensas ----------------------------------------------------------
INSERT INTO recompensas (nome, descricao, custo_pontos, estoque, disponivel) VALUES
    ('Vale-livro Eurofarma',    'Voucher para livros técnicos.',            800, 12, b'1'),
    ('Day-off de estudo',       'Um dia útil dedicado a estudos.',         1500,  5, b'1'),
    ('Certificado destaque',    'Certificado especial de destaque.',       3000, 20, b'1'),
    ('Mentoria 1:1',            'Sessão individual com especialista.',     2200,  8, b'1'),
    ('Voucher Café Eurofarma',  'Voucher de café na cantina.',              400, 30, b'1');

-- Presenças ------------------------------------------------------------
INSERT INTO presencas (matricula_id, data, presente, observacao) VALUES
    (1, '2024-10-21', b'1', NULL),
    (1, '2024-10-22', b'1', NULL),
    (1, '2024-10-23', b'0', 'Falta justificada'),
    (2, '2024-10-21', b'0', 'Não compareceu'),
    (2, '2024-10-22', b'1', NULL),
    (3, '2024-10-21', b'1', NULL);

-- Comunicados ----------------------------------------------------------
INSERT INTO comunicados (remetente_id, destinatario_id, assunto, mensagem, tipo, data_envio, lido) VALUES
    (6, 4, 'Engajamento da turma em queda',
     'Compliance Regulatório caiu 6% em 30 dias. Vamos revisar a carga do módulo 3?',
     'ATENCAO', '2024-10-22 14:22:00', b'1'),
    (6, 5, 'Parabéns pelo resultado',
     'Bioequivalência 23-A atingiu 91% de presença este mês.',
     'POSITIVO', '2024-10-22 09:10:00', b'1'),
    (4, 6, '2 alunos em prioridade de cuidado',
     'Turma tem 2 alunos sem acesso há 8+ dias. Recomendamos contato.',
     'CRITICO', '2024-10-22 15:00:00', b'0');

-- Resgates -------------------------------------------------------------
-- Marina (matricula 1, 4820 pontos) já resgatou um Vale-livro
INSERT INTO resgates (matricula_id, recompensa_id, status, data_resgate, pontos_gastos) VALUES
    (1, 1, 'ENTREGUE',   '2024-10-15 10:30:00', 800),
    (3, 5, 'SOLICITADO', '2024-10-22 14:45:00', 400);

-- Conversas Euri --------------------------------------------------------
INSERT INTO conversas_euri (usuario_id, titulo, iniciada_em, ativa) VALUES
    (1, 'Dúvidas sobre missões',       '2024-10-22 09:00:00', b'1'),
    (4, 'Como registrar presença',     '2024-10-22 10:15:00', b'0');

-- Mensagens Euri --------------------------------------------------------
INSERT INTO mensagens_euri (conversa_id, remetente, conteudo, enviada_em) VALUES
    (1, 'EURI',    'Oi, Marina! Eu sou a Euri 💙. Posso te ajudar com missões, presença, pontos e recompensas.', '2024-10-22 09:00:00'),
    (1, 'USUARIO', 'Como faço para concluir uma missão?',                                                        '2024-10-22 09:01:00'),
    (1, 'EURI',    'Suas missões estão em GET /api/v1/missoes. Ao concluir, os pontos são creditados na sua matrícula!', '2024-10-22 09:01:10'),
    (2, 'EURI',    'Olá, Dr. Renato! Aqui é a Euri. Como posso ajudar?',                                          '2024-10-22 10:15:00'),
    (2, 'USUARIO', 'Como registrar presença da turma?',                                                           '2024-10-22 10:16:00'),
    (2, 'EURI',    'Use POST /api/v1/presencas informando matriculaId, data e presente=true/false.',              '2024-10-22 10:16:10');
