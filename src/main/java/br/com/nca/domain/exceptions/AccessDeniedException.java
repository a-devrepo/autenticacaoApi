package br.com.nca.domain.exceptions;

public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = -9179162250283582565L;

    @Override
    public String getMessage() {
        return "Credenciais inválidas";
    }
}
