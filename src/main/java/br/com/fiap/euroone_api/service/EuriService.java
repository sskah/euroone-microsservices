package br.com.fiap.euroone_api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.euroone_api.dto.euri.ConversaEuriCreateRequest;
import br.com.fiap.euroone_api.dto.euri.ConversaEuriUpdateRequest;
import br.com.fiap.euroone_api.dto.euri.MensagemEuriCreateRequest;
import br.com.fiap.euroone_api.exception.ResourceNotFoundException;
import br.com.fiap.euroone_api.model.ConversaEuri;
import br.com.fiap.euroone_api.model.MensagemEuri;
import br.com.fiap.euroone_api.model.Usuario;
import br.com.fiap.euroone_api.model.enums.PerfilUsuario;
import br.com.fiap.euroone_api.model.enums.RemetenteEuri;
import br.com.fiap.euroone_api.repository.ConversaEuriRepository;
import br.com.fiap.euroone_api.repository.MensagemEuriRepository;

/**
 * Camada de negócio para a assistente Euri.
 *
 * Gera respostas por regras locais (fallback), espelhando o comportamento
 * do protótipo Flutter — que também usa um "chat com fallback local" quando
 * o serviço de IA remoto não está disponível. Em uma versão produtiva, este
 * service poderia delegar a geração para um provedor de LLM.
 */
@Service
public class EuriService {

    private static final DateTimeFormatter FORMATADOR_TITULO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private ConversaEuriRepository conversaRepository;

    @Autowired
    private MensagemEuriRepository mensagemRepository;

    @Autowired
    private UsuarioService usuarioService;

    // ---------- Conversas ----------

    @Transactional
    public ConversaEuri iniciarConversa(ConversaEuriCreateRequest dto) {
        Usuario usuario = usuarioService.findById(dto.getUsuarioId());

        ConversaEuri conversa = new ConversaEuri();
        conversa.setUsuario(usuario);
        conversa.setTitulo(dto.getTitulo() == null || dto.getTitulo().isBlank()
                ? "Conversa de " + LocalDateTime.now().format(FORMATADOR_TITULO)
                : dto.getTitulo());
        conversa.setIniciadaEm(LocalDateTime.now());
        conversa.setAtiva(true);
        conversa = conversaRepository.save(conversa);

        // Registra a mensagem de boas-vindas da Euri para dar contexto imediato.
        MensagemEuri boasVindas = new MensagemEuri();
        boasVindas.setConversa(conversa);
        boasVindas.setRemetente(RemetenteEuri.EURI);
        boasVindas.setConteudo(saudacaoInicial(usuario));
        boasVindas.setEnviadaEm(LocalDateTime.now());
        mensagemRepository.save(boasVindas);

        return conversa;
    }

    public ConversaEuri atualizarConversa(Long id, ConversaEuriUpdateRequest dto) {
        ConversaEuri existente = findConversaById(id);
        if (dto.getTitulo() != null && !dto.getTitulo().isBlank()) {
            existente.setTitulo(dto.getTitulo());
        }
        if (dto.getAtiva() != null) {
            existente.setAtiva(dto.getAtiva());
        }
        return conversaRepository.save(existente);
    }

    public List<ConversaEuri> listarConversas(Long usuarioId) {
        if (usuarioId == null) {
            return conversaRepository.findAll();
        }
        return conversaRepository.findByUsuarioIdOrderByIniciadaEmDesc(usuarioId);
    }

    public ConversaEuri findConversaById(Long id) {
        return conversaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversa Euri", id));
    }

    @Transactional
    public void deletarConversa(Long id) {
        if (!conversaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Conversa Euri", id);
        }
        mensagemRepository.deleteByConversaId(id);
        conversaRepository.deleteById(id);
    }

    // ---------- Mensagens ----------

    public List<MensagemEuri> listarMensagens(Long conversaId) {
        findConversaById(conversaId); // valida existência
        return mensagemRepository.findByConversaIdOrderByEnviadaEmAsc(conversaId);
    }

    /**
     * Envia uma mensagem do usuário e gera automaticamente a resposta da Euri.
     * Retorna um array com [mensagemUsuario, respostaEuri] nessa ordem.
     */
    @Transactional
    public MensagemEuri[] enviarMensagem(Long conversaId, MensagemEuriCreateRequest dto) {
        ConversaEuri conversa = findConversaById(conversaId);
        if (Boolean.FALSE.equals(conversa.getAtiva())) {
            throw new IllegalArgumentException("Não é possível enviar mensagens em uma conversa encerrada.");
        }

        // 1) Persiste a mensagem do usuário
        MensagemEuri msgUsuario = new MensagemEuri();
        msgUsuario.setConversa(conversa);
        msgUsuario.setRemetente(RemetenteEuri.USUARIO);
        msgUsuario.setConteudo(dto.getConteudo());
        msgUsuario.setEnviadaEm(LocalDateTime.now());
        msgUsuario = mensagemRepository.save(msgUsuario);

        // 2) Gera e persiste a resposta da Euri
        MensagemEuri msgEuri = new MensagemEuri();
        msgEuri.setConversa(conversa);
        msgEuri.setRemetente(RemetenteEuri.EURI);
        msgEuri.setConteudo(gerarResposta(dto.getConteudo(), conversa.getUsuario()));
        msgEuri.setEnviadaEm(LocalDateTime.now());
        msgEuri = mensagemRepository.save(msgEuri);

        return new MensagemEuri[] { msgUsuario, msgEuri };
    }

    // ---------- Geração de resposta (fallback local, sem IA remota) ----------

    private String saudacaoInicial(Usuario usuario) {
        String primeiroNome = usuario.getNome().split(" ")[0];
        return switch (usuario.getPerfil()) {
            case EDUCANDO -> String.format(
                "Oi, %s! Eu sou a Euri 💙. Posso te ajudar com missões, presença, "
              + "pontos e recompensas. Me conta o que você quer saber?", primeiroNome);
            case EDUCADOR -> String.format(
                "Olá, %s! Aqui é a Euri. Posso te apoiar com indicadores da turma, "
              + "presença x engajamento e recomendações de intervenção. Como posso ajudar?", primeiroNome);
            case GESTAO -> String.format(
                "Olá, %s. Sou a Euri, sua assistente de gestão pedagógica. "
              + "Posso te ajudar com indicadores executivos, cursos e fila de cuidado.", primeiroNome);
        };
    }

    /**
     * Gerador de resposta baseado em palavras-chave.
     * Se nenhuma palavra-chave bater, devolve uma resposta genérica.
     */
    private String gerarResposta(String perguntaOriginal, Usuario usuario) {
        String pergunta = perguntaOriginal.toLowerCase(Locale.ROOT);
        PerfilUsuario perfil = usuario.getPerfil();

        if (contem(pergunta, "presença", "presenca", "falta", "faltas")) {
            return perfil == PerfilUsuario.EDUCADOR
                ? "Você pode registrar presenças em POST /api/v1/presencas informando "
                + "matriculaId, data e presente=true/false. Para ver o histórico use "
                + "GET /api/v1/presencas."
                : "Sua presença é registrada pelo educador da turma. Você pode acompanhar "
                + "seu histórico em GET /api/v1/presencas.";
        }
        if (contem(pergunta, "missão", "missao", "missões", "missoes", "pontos")) {
            return "Suas missões estão em GET /api/v1/missoes. Ao concluir uma missão "
                 + "seus pontos são creditados na matrícula correspondente — dá para "
                 + "gastá-los depois no catálogo de recompensas!";
        }
        if (contem(pergunta, "recompensa", "recompensas", "resgate", "resgates", "prêmio", "premio")) {
            return "O catálogo de recompensas fica em GET /api/v1/recompensas. Para "
                 + "resgatar um item basta chamar POST /api/v1/resgates informando "
                 + "matriculaId e recompensaId — a Euri desconta seus pontos e reserva "
                 + "o item no estoque.";
        }
        if (contem(pergunta, "curso", "cursos", "turma", "turmas")) {
            return "Os cursos ficam em GET /api/v1/cursos e as turmas em GET /api/v1/turmas. "
                 + "Cada turma tem um educador responsável e um curso vinculado.";
        }
        if (contem(pergunta, "comunicado", "comunicados", "mensagem interna")) {
            return "Comunicados internos entre gestão e educadores ficam em "
                 + "GET /api/v1/comunicados. É possível filtrar por lido/não lido "
                 + "atualizando o campo 'lido' via PUT.";
        }
        if (contem(pergunta, "oi", "olá", "ola", "bom dia", "boa tarde", "boa noite")) {
            return "Oi! Estou aqui para ajudar. Você pode me perguntar sobre presença, "
                 + "missões, pontos, recompensas, cursos ou comunicados.";
        }
        if (contem(pergunta, "obrigado", "obrigada", "valeu", "tchau", "até logo", "ate logo")) {
            return "Sempre à disposição! Qualquer dúvida, é só chamar a Euri de novo. 💙";
        }

        // Resposta genérica quando nenhuma palavra-chave é reconhecida
        return "Ainda estou aprendendo — mas posso te ajudar com temas como presença, "
             + "missões, pontos, recompensas, cursos, turmas e comunicados. "
             + "Reformule sua pergunta usando um desses temas que eu tento te responder!";
    }

    private boolean contem(String texto, String... palavras) {
        for (String p : palavras) {
            if (texto.contains(p)) return true;
        }
        return false;
    }
}
