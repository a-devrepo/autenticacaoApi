package br.com.nca.domain.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CriarUsuarioResponse {

    private UUID id;
    private String nome;
    private String email;
    private LocalDateTime dataHoraCriacao;
}
