package br.com.fiap.euroone_api.exception;

/**
 * Exceção lançada quando um recurso não é encontrado no banco de dados.
 * Capturada pelo {@link GlobalExceptionHandler} e transformada em resposta 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String recurso, Object id) {
        super(String.format("%s com id %s não encontrado(a).", recurso, id));
    }
}
