# 📤 Publicando a EuroOne API no Docker Hub

Este documento descreve o passo a passo para publicar a imagem da EuroOne API no [Docker Hub](https://hub.docker.com/), tornando-a acessível a qualquer pessoa com um `docker pull`. É pré-requisito para o deploy da aplicação em ambientes de nuvem (AWS ECS, Render, Railway, Fly.io, Google Cloud Run, etc.).

> ℹ️ Substitua `<seu-usuario>` em todos os comandos abaixo pelo seu username real do Docker Hub.

---

## 📋 Sumário

- [Pré-requisitos](#-pré-requisitos)
- [1. Criar conta no Docker Hub](#1-criar-conta-no-docker-hub)
- [2. Criar o repositório da imagem](#2-criar-o-repositório-da-imagem)
- [3. Gerar um Personal Access Token (PAT)](#3-gerar-um-personal-access-token-pat)
- [4. Fazer login pelo terminal](#4-fazer-login-pelo-terminal)
- [5. Construir a imagem localmente](#5-construir-a-imagem-localmente)
- [6. Renomear a imagem com uma tag do Docker Hub](#6-renomear-a-imagem-com-uma-tag-do-docker-hub)
- [7. Publicar a imagem (docker push)](#7-publicar-a-imagem-docker-push)
- [8. Validar a publicação](#8-validar-a-publicação)
- [9. Rodar a imagem publicada em qualquer máquina](#9-rodar-a-imagem-publicada-em-qualquer-máquina)
- [10. Publicando novas versões](#10-publicando-novas-versões)
- [Boas práticas de versionamento](#-boas-práticas-de-versionamento)
- [Troubleshooting](#-troubleshooting)

---

## ✅ Pré-requisitos

- Imagem local da API já construída (`docker build -t euroone-api:1.0.0 .` já executado com sucesso — ver `README.md` seção "Como rodar a aplicação com Docker").
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando.
- Terminal (Git Bash, PowerShell, WSL, iTerm — qualquer um funciona).

---

## 1. Criar conta no Docker Hub

1. Acesse [https://hub.docker.com/](https://hub.docker.com/).
2. Clique em **Sign Up** e crie sua conta gratuita.
3. Confirme o email de verificação.

Depois de logado, seu perfil ficará em uma URL do tipo `https://hub.docker.com/u/<seu-usuario>`.

---

## 2. Criar o repositório da imagem

1. No Docker Hub, clique em **Repositories** → **Create a repository**.
2. Preencha:
   - **Name**: `euroone-api`
   - **Visibility**: `Public` (para que qualquer pessoa possa fazer `docker pull` — necessário para deploy em serviços gratuitos e para a avaliação da FIAP).
   - **Description** (opcional): `API REST do projeto EuroOne — Sprint 3 Java Microsserviços — FIAP × Eurofarma`.
3. Clique em **Create**.

O repositório final ficará em: `https://hub.docker.com/r/<seu-usuario>/euroone-api`.

---

## 3. Gerar um Personal Access Token (PAT)

O Docker Hub não aceita mais a senha da conta em CLI — é obrigatório usar um Personal Access Token.

1. Acesse [https://app.docker.com/settings/personal-access-tokens](https://app.docker.com/settings/personal-access-tokens).
2. Clique em **Generate new token**.
3. Preencha:
   - **Description**: `EuroOne API - CLI push` (ou algo que te ajude a lembrar depois).
   - **Access permissions**: `Read, Write, Delete` (necessário para publicar imagens).
   - **Expiration**: escolha um prazo (ex.: 90 dias).
4. Clique em **Generate**.
5. **Copie o token exibido na tela agora** — ele **não será mostrado novamente**. Guarde em um gerenciador de senhas.

---

## 4. Fazer login pelo terminal

Na raiz do projeto (ou de qualquer lugar), execute:

```bash
docker login -u <seu-usuario>
```

Quando o Docker pedir a senha, cole o **PAT** que você copiou no passo anterior (não a senha da sua conta).

Saída esperada:

```text
Login Succeeded
```

> 💡 O login fica salvo em `~/.docker/config.json` até você rodar `docker logout`. Não precisa logar de novo em todo comando.

---

## 5. Construir a imagem localmente

Se ainda não fez, construa a imagem da API a partir da raiz do projeto:

```bash
docker build -t euroone-api:1.0.0 .
```

Confirme que a imagem foi criada:

```bash
docker images
```

Você deve ver uma linha parecida com:

```text
REPOSITORY          TAG       IMAGE ID       CREATED         SIZE
euroone-api         1.0.0     abc123def456   2 minutes ago   198MB
```

---

## 6. Renomear a imagem com uma tag do Docker Hub

O Docker Hub exige que o nome da imagem siga o formato `<usuario>/<repositorio>:<versao>`. Como a imagem local está com o nome `euroone-api:1.0.0`, precisamos criar uma **tag** apontando para o repositório do Docker Hub.

O comando `docker tag` **não copia nem duplica a imagem** — só cria um "apelido" adicional para a mesma imagem local:

```bash
docker tag euroone-api:1.0.0 <seu-usuario>/euroone-api:1.0.0
```

Exemplo com um usuário real:

```bash
docker tag euroone-api:1.0.0 lucasbel/euroone-api:1.0.0
```

Confirme que a tag foi criada:

```bash
docker images
```

Agora você verá duas entradas apontando para o mesmo `IMAGE ID`:

```text
REPOSITORY                    TAG       IMAGE ID       CREATED         SIZE
euroone-api                   1.0.0     abc123def456   3 minutes ago   198MB
lucasbel/euroone-api          1.0.0     abc123def456   3 minutes ago   198MB
```

---

## 7. Publicar a imagem (docker push)

```bash
docker push <seu-usuario>/euroone-api:1.0.0
```

Exemplo:

```bash
docker push lucasbel/euroone-api:1.0.0
```

Você verá o Docker enviando cada camada da imagem:

```text
The push refers to repository [docker.io/lucasbel/euroone-api]
5f70bf18a086: Pushed
a3f4d8b1e2c9: Pushed
7d9b2a1f6e3c: Pushed
...
1.0.0: digest: sha256:... size: 1789
```

Dependendo da sua conexão, isso pode levar de 1 a 5 minutos na primeira vez (as camadas do Alpine + JRE são novas). Em pushes subsequentes, apenas as camadas alteradas são enviadas.

---

## 8. Validar a publicação

Acesse `https://hub.docker.com/r/<seu-usuario>/euroone-api/tags` no navegador. A tag `1.0.0` deve aparecer listada, com data de publicação e tamanho da imagem.

Para validar pelo terminal (de qualquer máquina):

```bash
docker pull <seu-usuario>/euroone-api:1.0.0
```

Se o pull funcionar em uma máquina que **nunca fez o build**, a imagem está publicamente disponível.

---

## 9. Rodar a imagem publicada em qualquer máquina

Depois de publicada, qualquer pessoa (ou serviço de nuvem) pode rodar a API sem precisar clonar o repositório ou instalar o Java:

```bash
# 1. Sobe o MySQL (igual ao README principal)
docker run -d --name euroone_mysql --rm \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_DATABASE=euroone \
  -p 3306:3306 \
  mysql:8.0

# 2. Sobe a API a partir da imagem do Docker Hub (repare que NÃO tem docker build)
docker run -d --name euroone_api --rm -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=euroone \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=default \
  <seu-usuario>/euroone-api:1.0.0
```

O Docker vai fazer `pull` automaticamente da imagem se ela ainda não estiver na máquina.

---

## 10. Publicando novas versões

Toda vez que você alterar o código e quiser publicar uma nova versão, siga os passos **5 → 6 → 7** com um número de versão maior:

```bash
# 1. Nova build local
docker build -t euroone-api:1.1.0 .

# 2. Nova tag apontando para o Docker Hub
docker tag euroone-api:1.1.0 <seu-usuario>/euroone-api:1.1.0

# 3. Publica
docker push <seu-usuario>/euroone-api:1.1.0
```

Você pode manter várias versões no mesmo repositório (`1.0.0`, `1.1.0`, `2.0.0`, etc.). Cada uma ficará como uma "tag" distinta na página do Docker Hub.

Também é comum publicar uma tag `latest` apontando para a versão mais recente:

```bash
docker tag euroone-api:1.1.0 <seu-usuario>/euroone-api:latest
docker push <seu-usuario>/euroone-api:latest
```

> ⚠️ Cuidado com `latest`: ele muda toda vez que você publica. Para produção, sempre referencie uma versão específica (`1.1.0`) e não `latest`, para evitar surpresas.

---

## 🏷️ Boas práticas de versionamento

Adote **[Semantic Versioning](https://semver.org/lang/pt-BR/)** (`MAJOR.MINOR.PATCH`):

| Componente | Quando incrementar                                         | Exemplo de mudança                          |
|------------|------------------------------------------------------------|---------------------------------------------|
| `MAJOR`    | Mudança incompatível na API (ex.: campo removido do JSON)  | `1.5.2` → `2.0.0`                           |
| `MINOR`    | Nova funcionalidade compatível (ex.: novo endpoint)        | `1.5.2` → `1.6.0`                           |
| `PATCH`    | Correção de bug ou ajuste interno                          | `1.5.2` → `1.5.3`                           |

Assim, um consumidor da sua API sabe imediatamente pelo número da tag o impacto de subir de uma versão para outra.

---

## 🛠️ Troubleshooting

### `denied: requested access to the resource is denied`

Você não fez login, ou o PAT expirou / não tem permissão de **Write**. Rode:

```bash
docker logout
docker login -u <seu-usuario>
```

E cole um PAT válido com permissão de escrita.

### `docker push` fica travado / muito lento

- Verifique sua conexão. As primeiras camadas do Alpine podem levar alguns minutos.
- Se cair no meio, apenas rode `docker push` de novo — ele retoma de onde parou.

### O nome da imagem local não bate com o repositório

Se você criou o repositório no Docker Hub como `euroone-api` mas fez `docker tag ... <seu-usuario>/euroone_api:1.0.0` (com underscore), o push vai falhar. **O nome no `docker tag` precisa ser exatamente igual** ao nome do repositório no Docker Hub, incluindo hífens vs. underscores.

### Quero tornar o repositório privado depois

No Docker Hub → **Repositories** → **euroone-api** → **Settings** → **Visibility** → **Private**. Contas gratuitas suportam 1 repositório privado; para mais é preciso plano pago.

### Como excluir uma tag publicada por engano

No Docker Hub → **Repositories** → **euroone-api** → **Tags** → clique no menu **⋮** ao lado da tag → **Delete tag**.

---

## 🎯 Resumo do fluxo completo

Depois que você fez o setup inicial (passos 1 a 4), publicar uma nova versão é sempre este ciclo de **3 comandos**:

```bash
docker build -t euroone-api:X.Y.Z .
docker tag   euroone-api:X.Y.Z <seu-usuario>/euroone-api:X.Y.Z
docker push  <seu-usuario>/euroone-api:X.Y.Z
```

Simples assim. ✅
