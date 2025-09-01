package br.com.nca.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CriarUsuarioRequest {

    @NotBlank(message = "Nome é obrigatório.")
    @Size(min = 6, max = 100, message = "Nome deve ter entre 6 e 100 caracteres.")
    @Pattern(regexp = "^[A-Za-zÀ-Üà-ü\\s]{6,100}$", 
    message = "O nome só pode ter letras e espaços e deve conter de 6 a 100 caracteres.")
    private String nome;

    @NotBlank(message = "Email é obrigatório.")
    @Email(message = "Email inválido.")
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9])(?!.*\\s)(?!.*(.).*\\1).{8,}$"
    , message = "A senha deve ter pelo menos 8 caracteres, com maiúsculas, minúsculas, número, símbolo, sem espaços nem caracteres repetidos.")
    private String senha;
}