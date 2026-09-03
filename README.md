# EuroOne API — Sprint 3 (Java Microsserviços)

API REST em **Java + Spring Boot** que representa a solução definida pela equipe **IV-ONE** nas Sprints 1 e 2 do Challenge **FIAP × Eurofarma**.

O EuroOne é uma plataforma de aprendizagem em saúde que centraliza a jornada de educandos, educadores e gestão em uma experiência única com acompanhamento de presença, engajamento, missões gamificadas, recompensas, resgates, comunicados internos e a assistente conversacional **Euri**.

A aplicação segue separação clara de responsabilidades entre **Controllers**, **Services**, **Repositories**, **Models**, **DTOs** e **Mappers**, com persistência em MySQL e documentação interativa via Swagger/OpenAPI.

---

## 📋 Sumário

- [Tecnologias utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Arquitetura da aplicação](#-arquitetura-da-aplicação)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Entidades do domínio](#-entidades-do-domínio)
- [Perfis de execução (profiles)](#-perfis-de-execução-profiles)
- [Como rodar a aplicação com Docker](#-como-rodar-a-aplicação-com-docker)
  - [1. Subindo o banco de dados MySQL](#1-subindo-o-banco-de-dados-mysql)
  - [2. Construindo a imagem da API](#2-construindo-a-imagem-da-api)
  - [3. Variáveis de ambiente necessárias](#3-variáveis-de-ambiente-necessárias)
  - [4. Executando a aplicação com docker run](#4-executando-a-aplicação-com-docker-run)
  - [5. Acessando o Swagger](#5-acessando-o-swagger)
  - [6. Testando o banco via DBeaver](#6-testando-o-banco-via-dbeaver)
  - [7. Encerrando os containers](#7-encerrando-os-containers)
- [Rodando a partir do código-fonte (desenvolvimento local)](#-rodando-a-partir-do-código-fonte-desenvolvimento-local)
- [Endpoints disponíveis](#-endpoints-disponíveis)
- [Exemplos de requisições](#-exemplos-de-requisições)
- [Correspondência com as Sprints 1 e 2](#-correspondência-com-as-sprints-1-e-2)
- [Autores](#-autores)

---

## 🚀 Tecnologias utilizadas

- **Java 17**
- **Spring Boot 3.3.5**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Boot DevTools
  - Spring Validation
- **MySQL 8**
- **Maven** (gerenciador de dependências)
- **Lombok** (redução de boilerplate)
- **ModelMapper** (mapeamento entre DTOs e Models)
- **SpringDoc OpenAPI / Swagger UI** (documentação interativa)
- **Docker** (empacotamento e deploy)

---

## ✅ Pré-requisitos

Antes de começar, você precisa ter instalado na sua máquina:

- [Java JDK 17+](https://adoptium.net/) *(apenas se for rodar localmente sem Docker)*
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/) *(para clonar o repositório)*
- [DBeaver](https://dbeaver.io/) *(para inspecionar o banco de dados)*

---

## 🏗️ Arquitetura da aplicação

A aplicação segue arquitetura em camadas, garantindo que a camada de endpoints (`controller`) **nunca acesse diretamente** a camada de persistência (`repository`):

```
Controller  →  Service (regras de negócio)  →  Repository (JPA)  →  Banco (MySQL)
     ↑                       ↓
     └────  DTO ← Mapper ← Model (Entity) ─┘
```

**Camadas:**

- **Controller** — recebe requisições HTTP, delega para o Service, retorna DTOs.
- **Service** — concentra as regras de negócio, unicidade, validação cruzada entre entidades.
- **Repository** — interface Spring Data JPA para acesso ao banco.
- **Model** — entidades JPA persistidas no MySQL.
- **DTO** — objetos de transporte separando o que entra e o que sai da API.
- **Mapper** — converte Entity ↔ DTO usando ModelMapper.
- **Exception** — `GlobalExceptionHandler` centraliza tratamento de erros (400 / 404 / 500).

**Pontos importantes:**

- Entrada e saída sempre via DTOs (`*CreateRequest`, `*UpdateRequest`, `*Response`) — as entidades JPA nunca são expostas nas rotas.
- Validação com Bean Validation (`@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`, `@Max`).
- Todas as rotas versionadas em `/api/v1/...` (controlado pela propriedade `api.version`).
- Documentação automática com Swagger UI.

---

## 📂 Estrutura do projeto

```text
euroone-api/
├── src/
│   └── main/
│       ├── java/br/com/fiap/euroone_api/
│       │   ├── Application.java
│       │   ├── config/                     (ModelMapper + OpenAPI)
│       │   ├── controller/                 (10 REST Controllers)
│       │   ├── service/                    (10 Services)
│       │   ├── repository/                 (10 Repositories)
│       │   ├── model/                      (11 Entidades + 5 Enums)
│       │   │   └── enums/
│       │   ├── dto/                        (DTOs por recurso)
│       │   │   ├── usuario/  curso/  turma/  matricula/
│       │   │   ├── missao/  recompensa/  presenca/  comunicado/
│       │   │   ├── resgate/                (Resgate de recompensas)
│       │   │   └── euri/                   (Chatbot Euri)
│       │   └── exception/
│       └── resources/
│           ├── application.properties         (profile default)
│           ├── application-dev.properties     (profile dev - local sem Docker)
│           └── application-prd.properties     (profile prd - produção)
├── db/
│   └── migrations/
│       └── V1__initial_schema.sql             (schema + seed data das 11 tabelas)
├── Dockerfile
├── .dockerignore
├── pom.xml
└── README.md
```

---

## 🧩 Entidades do domínio

Mapeadas a partir do protótipo standalone e dos modelos Flutter do projeto EuroOne — **11 entidades no total**:

| Entidade         | Descrição                                                                                       |
|------------------|-------------------------------------------------------------------------------------------------|
| **Usuário**      | Qualquer usuário da plataforma; enum `PerfilUsuario` (EDUCANDO / EDUCADOR / GESTAO).            |
| **Curso**        | Curso oferecido (ex.: Farmacovigilância, Bioequivalência, Epidemiologia).                       |
| **Turma**        | Turma vinculada a um curso e a um educador responsável.                                         |
| **Matrícula**    | Vínculo educando ↔ turma, com progresso (0-100) e pontos.                                       |
| **Missão**       | Missão gamificada atribuída a um educando (título, pontos, prazo, `StatusMissao`).              |
| **Recompensa**   | Item resgatável com pontos (nome, custo, estoque, disponibilidade).                             |
| **Resgate**      | Educando gasta pontos para pegar uma recompensa (`StatusResgate`: SOLICITADO → APROVADO → ENTREGUE / CANCELADO). |
| **Presença**     | Registro de presença/falta por matrícula em uma data (única por combinação).                    |
| **Comunicado**   | Comunicação interna entre usuários (`TipoComunicado`: POSITIVO / ATENCAO / CRITICO).            |
| **Conversa Euri** | Sessão de chat entre um usuário e a assistente Euri.                                           |
| **Mensagem Euri** | Turno individual de uma conversa (`RemetenteEuri`: USUARIO ou EURI).                           |

**Regras de negócio implementadas nos Services (não apenas CRUD):**

- Não é possível cadastrar dois usuários com o mesmo `email` ou `matricula`.
- Só usuários com perfil `EDUCANDO` podem ser matriculados ou receber missões.
- Só usuários com perfil `EDUCADOR` podem ser designados como educador de uma turma.
- Um educando não pode ter duas matrículas ativas para a mesma turma.
- Não é possível registrar duas presenças no mesmo dia para a mesma matrícula.
- Remetente e destinatário de um comunicado não podem ser o mesmo usuário.
- **Resgate**: valida saldo de pontos e estoque, debita pontos da matrícula e decrementa estoque. Cancelamento devolve pontos e estoque.
- **Euri**: a resposta é gerada por regras locais (fallback rule-based) espelhando o "chat com fallback local" do protótipo. Conversas encerradas não recebem novas mensagens.

---

## ⚙️ Perfis de execução (profiles)

A aplicação suporta três perfis, ativados pela variável `SPRING_PROFILES_ACTIVE`:

| Profile   | Arquivo                             | Uso indicado                                | ddl-auto | show-sql | createDatabaseIfNotExist |
|-----------|-------------------------------------|---------------------------------------------|----------|----------|--------------------------|
| `default` | `application.properties`            | Execução dentro do container Docker         | `update` | `true`   | ✅ Sim                    |
| `dev`     | `application-dev.properties`        | Execução local (fora do Docker, banco local)| `update` | `true`   | ✅ Sim                    |
| `prd`     | `application-prd.properties`        | Produção (banco e tabelas já existem)       | `none`   | `false`  | ❌ Não                    |

O Dockerfile define `ENV SPRING_PROFILES_ACTIVE=dev` como padrão, mas o valor pode ser sobrescrito no `docker run -e SPRING_PROFILES_ACTIVE=...`. Para o teste da imagem via Docker recomendamos o profile `default`.

---

## ▶️ Como rodar a aplicação com Docker

Toda a stack é executada com comandos `docker` puros. Siga os passos abaixo **na ordem**.

---

### 1. Subindo o banco de dados MySQL

A aplicação depende de um banco **MySQL 8**. O comando abaixo sobe um container MySQL e monta a pasta `db/migrations` em `/docker-entrypoint-initdb.d/`, o que faz o MySQL executar automaticamente o `V1__initial_schema.sql` na primeira inicialização — criando as 11 tabelas e populando dados de exemplo.

**Linux / Mac:**

```bash
docker run -d \
  --name euroone_mysql \
  --rm \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_DATABASE=euroone \
  -p 3306:3306 \
  -v "$(pwd)/db/migrations:/docker-entrypoint-initdb.d" \
  mysql:8.0
```

**Windows (PowerShell):**

```powershell
docker run -d `
  --name euroone_mysql `
  --rm `
  -e MYSQL_ROOT_PASSWORD=root_pwd `
  -e MYSQL_DATABASE=euroone `
  -p 3306:3306 `
  -v "${PWD}/db/migrations:/docker-entrypoint-initdb.d" `
  mysql:8.0
```

> ⏳ Aguarde ~30 segundos até o MySQL inicializar completamente antes de seguir. Você pode acompanhar com `docker logs -f euroone_mysql` — a inicialização estará pronta quando aparecer `ready for connections` duas vezes.

---

### 2. Construindo a imagem da API

O projeto inclui um `Dockerfile` com build multi-estágio (Maven + Eclipse Temurin 17 Alpine). Na raiz do projeto, execute:

```bash
docker build -t euroone-api:1.0.0 .
```

Para confirmar que a imagem foi criada:

```bash
docker images
```

---

### 3. Variáveis de ambiente necessárias

A imagem da API recebe toda a configuração de banco via variáveis de ambiente, passadas com a flag `-e` no `docker run`:

| Variável                  | Descrição                                       | Valor de exemplo          |
|---------------------------|-------------------------------------------------|---------------------------|
| `DB_SERVER_URL`           | Host do servidor MySQL                          | `host.docker.internal`    |
| `DB_SERVER_PORT`          | Porta do servidor MySQL                         | `3306`                    |
| `DB_SCHEMA`               | Nome do schema/banco de dados                   | `euroone`                 |
| `DB_USER`                 | Usuário do banco de dados                       | `root`                    |
| `DB_PWD`                  | Senha do banco de dados                         | `root_pwd`                |
| `SPRING_PROFILES_ACTIVE`  | Profile ativo (`default`, `dev` ou `prd`)       | `default`                 |

> 💡 **Sobre o `host.docker.internal`:** esse hostname permite que o container da API acesse o MySQL rodando na máquina host. No **Linux**, adicione também `--add-host=host.docker.internal:host-gateway` ao comando `docker run` (Docker Desktop no Windows/Mac já resolve o hostname automaticamente).

---

### 4. Executando a aplicação com `docker run`

O comando abaixo mapeia a porta **8080**, define o **profile** e passa as variáveis de ambiente para conexão com o banco.

#### Profile `default` — recomendado para testar a imagem

Cria/atualiza o schema automaticamente via Hibernate. É o profile ideal para o primeiro teste:

```bash
docker run -d \
  --name euroone_api \
  --rm \
  -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=euroone \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=default \
  euroone-api:1.0.0
```

> **No Linux**, adicione a linha `--add-host=host.docker.internal:host-gateway \` antes do `-p 8080:8080`.

#### Profile `prd` — execução em produção

Não altera o schema do banco. Exige que o banco **e as tabelas já existam** previamente (o que acontece se você seguiu o passo 1, já que o `V1__initial_schema.sql` foi executado automaticamente pelo container do MySQL):

```bash
docker run -d \
  --name euroone_api \
  --rm \
  -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=euroone \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=prd \
  euroone-api:1.0.0
```

Acompanhe o log da aplicação com:

```bash
docker logs -f euroone_api
```

A aplicação estará pronta quando o log exibir:

```text
Started Application in X.XXX seconds
```

---

### 5. Acessando o Swagger

Com a aplicação rodando, acesse a documentação interativa pelo navegador:

| Recurso        | URL                                            |
|----------------|------------------------------------------------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html          |
| OpenAPI JSON   | http://localhost:8080/v3/api-docs              |

Pelo Swagger UI é possível testar todos os endpoints diretamente do navegador: basta expandir a operação desejada, clicar em **Try it out**, preencher os dados e clicar em **Execute**.

Para validar rapidamente pelo terminal:

```bash
curl http://localhost:8080/api/v1/usuarios
```

---

### 6. Testando o banco via DBeaver

1. Abra o DBeaver → **Nova Conexão** → **MySQL**.
2. Preencha:
   - Server Host: `localhost`
   - Port: `3306`
   - Database: `euroone`
   - Username: `root`
   - Password: `root_pwd`
3. Clique em **Test Connection** (na primeira vez pode ser necessário instalar o driver).
4. Após conectar, expanda `euroone` → `Tables` para inspecionar as 11 tabelas criadas.

As tabelas já vêm populadas com dados de exemplo (7 usuários, 4 cursos, 3 turmas, 3 matrículas, 4 missões, 5 recompensas, 6 presenças, 3 comunicados, 2 resgates, 2 conversas Euri, 6 mensagens Euri) — resultado da execução automática do script `V1__initial_schema.sql`.

---

### 7. Encerrando os containers

Para parar a aplicação:

```bash
docker stop euroone_api
```

Para parar o banco:

```bash
docker stop euroone_mysql
```

Como usamos a flag `--rm` no `docker run`, os containers são removidos automaticamente ao parar. Se quiser remover a imagem da API também:

```bash
docker rmi euroone-api:1.0.0
```

---

> 📤 **Publicando a imagem no Docker Hub?** Consulte o guia [`dockerhub.md`](./dockerhub.md) com o passo-a-passo completo (criar conta, gerar PAT, `docker tag`, `docker push` e versionamento).

---

## 🛠️ Rodando a partir do código-fonte (desenvolvimento local)

Alternativa ao container, para quem deseja alterar o código da aplicação.

### 1. Subindo o banco de dados

Suba apenas o container do MySQL (passo 1 acima).

### 2. Rodando a API Spring Boot

Com o banco rodando, abra um terminal na raiz do projeto e execute (usando o profile `dev`, que já aponta para `localhost:3306`):

**Linux / Mac:**

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**Windows (PowerShell):**

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

A aplicação subirá em `http://localhost:8080` e as tabelas serão criadas automaticamente pelo Hibernate caso ainda não existam.

### 3. (Opcional) Executar o SQL manualmente

Se preferir criar as tabelas e dados via DBeaver (sem depender do volume Docker), abra o arquivo `db/migrations/V1__initial_schema.sql` no SQL Editor do DBeaver e execute-o (Alt+X).

---

## 🔌 Endpoints disponíveis

Todos os endpoints estão sob o prefixo versionado `/api/v1`. Cada recurso implementa **CRUD completo**:

| Recurso        | Rota base                                        | Operações principais                                       |
|----------------|--------------------------------------------------|-------------------------------------------------------------|
| Usuários       | `/api/v1/usuarios`                               | POST, GET (com filtro `?perfil=`), GET/{id}, PUT, DELETE   |
| Cursos         | `/api/v1/cursos`                                 | POST, GET, GET/{id}, PUT, DELETE                            |
| Turmas         | `/api/v1/turmas`                                 | POST, GET, GET/{id}, PUT, DELETE                            |
| Matrículas     | `/api/v1/matriculas`                             | POST, GET, GET/{id}, PUT, DELETE                            |
| Missões        | `/api/v1/missoes`                                | POST, GET, GET/{id}, PUT, DELETE                            |
| Recompensas    | `/api/v1/recompensas`                            | POST, GET, GET/{id}, PUT, DELETE                            |
| **Resgates**   | `/api/v1/resgates`                               | POST (solicita), GET (`?matriculaId=`), GET/{id}, PUT (muda status), DELETE |
| Presenças      | `/api/v1/presencas`                              | POST, GET, GET/{id}, PUT, DELETE                            |
| Comunicados    | `/api/v1/comunicados`                            | POST, GET, GET/{id}, PUT, DELETE                            |
| **Euri — conversas** | `/api/v1/euri/conversas`                    | POST (inicia), GET (`?usuarioId=`), GET/{id}, PUT, DELETE   |
| **Euri — mensagens** | `/api/v1/euri/conversas/{id}/mensagens`     | POST (envia e recebe resposta), GET (histórico da conversa) |

---

## 📝 Exemplos de requisições

Exemplos com `curl` que funcionam de imediato após seguir os passos acima.

### 🧠 Chatbot Euri — iniciar uma conversa

```bash
curl -X POST http://localhost:8080/api/v1/euri/conversas \
  -H "Content-Type: application/json" \
  -d '{
        "usuarioId": 1,
        "titulo": "Preciso de ajuda com missões"
      }'
```

Resposta (Euri já retorna com uma saudação automática):

```json
{
  "id": 3,
  "usuario": { "id": 1, "nome": "Marina Soares", "perfil": "EDUCANDO", ... },
  "titulo": "Preciso de ajuda com missões",
  "iniciadaEm": "2025-08-30T15:22:00",
  "ativa": true,
  "mensagens": [
    {
      "id": 7,
      "remetente": "EURI",
      "conteudo": "Oi, Marina! Eu sou a Euri 💙. Posso te ajudar com missões, presença, pontos e recompensas...",
      "enviadaEm": "2025-08-30T15:22:00"
    }
  ]
}
```

### 🧠 Chatbot Euri — enviar uma mensagem e receber resposta

```bash
curl -X POST http://localhost:8080/api/v1/euri/conversas/3/mensagens \
  -H "Content-Type: application/json" \
  -d '{
        "conteudo": "Como faço para resgatar uma recompensa?"
      }'
```

Resposta:

```json
{
  "mensagemUsuario": {
    "id": 8,
    "remetente": "USUARIO",
    "conteudo": "Como faço para resgatar uma recompensa?",
    "enviadaEm": "2025-08-30T15:23:00"
  },
  "respostaEuri": {
    "id": 9,
    "remetente": "EURI",
    "conteudo": "O catálogo de recompensas fica em GET /api/v1/recompensas. Para resgatar um item basta chamar POST /api/v1/resgates informando matriculaId e recompensaId...",
    "enviadaEm": "2025-08-30T15:23:00"
  }
}
```

### 🧠 Chatbot Euri — listar histórico

```bash
# Todas as conversas de um usuário
curl "http://localhost:8080/api/v1/euri/conversas?usuarioId=1"

# Mensagens de uma conversa específica
curl http://localhost:8080/api/v1/euri/conversas/3/mensagens
```

### 🎁 Resgatar uma recompensa

```bash
curl -X POST http://localhost:8080/api/v1/resgates \
  -H "Content-Type: application/json" \
  -d '{
        "matriculaId": 1,
        "recompensaId": 5
      }'
```

Se a matrícula tiver pontos suficientes e a recompensa tiver estoque, o service **desconta os pontos**, **decrementa o estoque** e cria o resgate com status `SOLICITADO`.

### 🎁 Aprovar / entregar / cancelar um resgate

```bash
curl -X PUT http://localhost:8080/api/v1/resgates/1 \
  -H "Content-Type: application/json" \
  -d '{ "status": "APROVADO" }'

curl -X PUT http://localhost:8080/api/v1/resgates/1 \
  -H "Content-Type: application/json" \
  -d '{ "status": "ENTREGUE" }'
```

Se o status mudar para `CANCELADO`, os pontos são devolvidos à matrícula e o item retorna ao estoque.

### 👥 Listar usuários

```bash
curl http://localhost:8080/api/v1/usuarios
curl "http://localhost:8080/api/v1/usuarios?perfil=EDUCADOR"
```

### 📚 Criar um curso

```bash
curl -X POST http://localhost:8080/api/v1/cursos \
  -H "Content-Type: application/json" \
  -d '{
        "nome": "Introdução à Farmacologia",
        "trilha": "Regulatório",
        "cargaHoraria": 40,
        "descricao": "Curso introdutório sobre princípios farmacológicos."
      }'
```

### 🏫 Criar uma turma (vincula curso + educador)

```bash
curl -X POST http://localhost:8080/api/v1/turmas \
  -H "Content-Type: application/json" \
  -d '{
        "codigo": "25-A",
        "periodo": "Noturno",
        "sala": "Auditório 1",
        "cursoId": 1,
        "educadorId": 4
      }'
```

### 📝 Matricular um educando

```bash
curl -X POST http://localhost:8080/api/v1/matriculas \
  -H "Content-Type: application/json" \
  -d '{
        "educandoId": 1,
        "turmaId": 1,
        "dataMatricula": "2025-02-10",
        "progresso": 0,
        "pontos": 0
      }'
```

### 🎯 Criar uma missão

```bash
curl -X POST http://localhost:8080/api/v1/missoes \
  -H "Content-Type: application/json" \
  -d '{
        "titulo": "Leitura obrigatória: capítulo 3",
        "descricao": "Ler e resumir o capítulo 3.",
        "pontos": 150,
        "status": "PENDENTE",
        "prazo": "2025-03-15",
        "educandoId": 1
      }'
```

### ✅ Registrar uma presença

```bash
curl -X POST http://localhost:8080/api/v1/presencas \
  -H "Content-Type: application/json" \
  -d '{
        "matriculaId": 1,
        "data": "2025-02-11",
        "presente": true,
        "observacao": null
      }'
```

### 📣 Enviar um comunicado

```bash
curl -X POST http://localhost:8080/api/v1/comunicados \
  -H "Content-Type: application/json" \
  -d '{
        "remetenteId": 6,
        "destinatarioId": 4,
        "assunto": "Reunião de alinhamento",
        "mensagem": "Podemos alinhar as metas da turma sexta-feira?",
        "tipo": "ATENCAO"
      }'
```

### 🗑️ Remover um recurso

```bash
curl -X DELETE http://localhost:8080/api/v1/missoes/2
```

### ⚠️ Exemplo de resposta de erro (validação)

Requisição inválida (email malformado, nome faltando):

```json
{
  "timestamp": "2025-08-30T14:22:00.123",
  "status": 400,
  "error": "Erro de validação",
  "fields": {
    "nome": "O nome é obrigatório",
    "email": "Email inválido"
  }
}
```

Recurso não encontrado (404):

```json
{
  "timestamp": "2025-08-30T14:22:00.456",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Usuário com id 999 não encontrado(a)."
}
```

Regra de negócio violada (400):

```json
{
  "timestamp": "2025-08-30T14:22:00.789",
  "status": 400,
  "error": "Requisição inválida",
  "message": "Pontos insuficientes: a matrícula tem 300 pontos e a recompensa custa 800."
}
```

---

## 🎯 Correspondência com as Sprints 1 e 2

Esta API contempla os requisitos definidos na entrega da Sprint 3:

| Requisito da Sprint                              | Como está atendido nesta entrega                                                                                       |
|--------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| **Modelagem e persistência (20 pts)**            | 11 entidades JPA + 5 enums, campos obrigatórios com `@Column(nullable=false)`, persistência em MySQL 8.                |
| **Operações da API (25 pts)**                    | CRUD completo para os 10 recursos + endpoints específicos (envio de mensagens à Euri, mudança de status de Resgate).   |
| **Arquitetura e organização (20 pts)**           | Camadas `Controller → Service → Repository` (controllers nunca chamam repositories diretamente); Services concentram regras de negócio significativas (unicidade, saldo de pontos, geração de resposta da Euri, cascade de mensagens). |
| **Entrada, saída e validação (15 pts)**          | DTOs `*CreateRequest`, `*UpdateRequest`, `*Response`; validação com Bean Validation; entidades nunca expostas.         |
| **Versionamento e documentação (5 pts)**         | Rotas versionadas em `/api/v1/...` via `api.version`; Swagger UI em `/swagger-ui.html`.                                |
| **Execução e documentação (15 pts)**             | Este README + `Dockerfile` + `.dockerignore` + script SQL de migrations executam a stack completa em três comandos.    |

Alterações significativas em relação ao protótipo Flutter (Sprints 1 e 2): nenhuma redução — pelo contrário, todos os conceitos apresentados (perfis, cursos, turmas, matrículas, missões, recompensas, presenças, comunicados e a **assistente Euri**) foram implementados na API, e ainda ganharam persistência real em banco relacional em vez de dados mockados. A entidade `Resgate` foi adicionada para dar significado ao ciclo Missão → Pontos → Recompensa.

---

## 👥 Autores

Equipe **IV-ONE** — FIAP × Eurofarma
