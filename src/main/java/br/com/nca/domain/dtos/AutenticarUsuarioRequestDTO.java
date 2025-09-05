package br.com.nca.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AutenticarUsuarioRequestDTO {

    @NotBlank(message = "Email é obrigatório.")
    @Email(message = "Email inválido.")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String senha;
}