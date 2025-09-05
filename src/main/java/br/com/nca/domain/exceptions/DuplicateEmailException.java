package br.com.nca.domain.exceptions;

public class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = -4767996807420311300L;

    @Override
    public String getMessage() {
        return "O email informado já está cadastrado";
    }
}