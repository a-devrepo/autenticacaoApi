package br.com.nca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nca.domain.dtos.AutenticarUsuarioRequestDTO;
import br.com.nca.domain.dtos.AutenticarUsuarioResponseDTO;
import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;
import br.com.nca.domain.dtos.CriarUsuarioResponseDTO;
import br.com.nca.domain.services.UsuarioService;
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

    private UsuarioService usuarioService;
    
    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
                        schema = @Schema(implementation = CriarUsuarioResponseDTO.class)
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
    public ResponseEntity<CriarUsuarioResponseDTO> criar(
            @Valid @RequestBody CriarUsuarioRequestDTO request){
       
        var response = usuarioService.criarUsuario(request);
        
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
                        schema = @Schema(implementation = AutenticarUsuarioResponseDTO.class)
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
    public ResponseEntity<AutenticarUsuarioResponseDTO> autenticar(
            @Valid @RequestBody AutenticarUsuarioRequestDTO request){
        
        var response = usuarioService.autenticarUsuario(request);
                
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}