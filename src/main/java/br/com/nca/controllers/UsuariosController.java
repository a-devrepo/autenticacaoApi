package br.com.nca.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nca.domain.dtos.AutenticarUsuarioRequest;
import br.com.nca.domain.dtos.AutenticarUsuarioResponse;
import br.com.nca.domain.dtos.CriarUsuarioRequest;
import br.com.nca.domain.dtos.CriarUsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuariosController {

    private UUID usuarioID;
    
    public UsuariosController() {
        usuarioID = UUID.randomUUID();
    }
    
    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário no sistema.",
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CriarUsuarioResponse.class)
                    )
                ),
                @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(mediaType = "application/json")
                )
            }
        )
    @PostMapping("/criar")
    public ResponseEntity<CriarUsuarioResponse> criar(
            @Valid @RequestBody CriarUsuarioRequest criarUsuarioRequest){
       
        var response = CriarUsuarioResponse.builder()
        .id(usuarioID)
        .nome(criarUsuarioRequest.getNome())
        .email(criarUsuarioRequest.getEmail())
        .dataHoraCriacao(LocalDateTime.now())
        .build();
        
        return ResponseEntity
                .status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica um usuário e retorna um token de acesso.",
            responses = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação bem-sucedida",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AutenticarUsuarioResponse.class)
                    )
                ),
                @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json")
                )
            }
        )
    @PostMapping("/autenticar")
    public ResponseEntity<AutenticarUsuarioResponse> autenticar(
            @Valid @RequestBody AutenticarUsuarioRequest autenticarUsuarioRequest){
        
        var response = AutenticarUsuarioResponse.builder()
                .id(usuarioID)
                .nome("user")
                .email(autenticarUsuarioRequest.getEmail())
                .dataHoraAcesso(LocalDateTime.now())
                .dataHoraExpiracao(LocalDateTime.now().plusHours(1))
                .token("sldsldkoewirweirow003-203-2380392fsdfdslsldsk00920923029")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}