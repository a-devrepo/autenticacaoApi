package br.com.nca.domain.services;

import java.time.LocalDateTime;

import br.com.nca.domain.dtos.AutenticarUsuarioRequestDTO;
import br.com.nca.domain.dtos.AutenticarUsuarioResponseDTO;
import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;
import br.com.nca.domain.dtos.CriarUsuarioResponseDTO;
import br.com.nca.domain.entities.Usuario;
import br.com.nca.domain.exceptions.DuplicateEmailException;
import br.com.nca.domain.repositories.UsuarioRepository;
import br.com.nca.helpers.CryptoHelper;

public class UsuarioService {

    private UsuarioRepository repository;
    private CryptoHelper cryptoHelper;
    
    public UsuarioService(UsuarioRepository repository, CryptoHelper cryptoHelper) {
        this.repository = repository;
        this.cryptoHelper = cryptoHelper;
    }
    
    public CriarUsuarioResponseDTO criarUsuario(CriarUsuarioRequestDTO requestDto) {
        
        if(repository.findByUserEmail(requestDto.getEmail())!= null) {
            throw new DuplicateEmailException();
        }
        
        var usuario = requestToEntity(requestDto);
        
        repository.save(usuario);
        
        return entityToResponse(usuario);
    }
    
    public AutenticarUsuarioResponseDTO autenticarUsuario(AutenticarUsuarioRequestDTO requestDto) {
        return null;
    }
    
    private Usuario requestToEntity(CriarUsuarioRequestDTO dto) {
        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(cryptoHelper.getSha256(dto.getSenha()))
                .dataCriacao(LocalDateTime.now())
                .build();
    }
    
    private CriarUsuarioResponseDTO entityToResponse(Usuario usuario) {
        return CriarUsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataHoraCriacao(usuario.getDataCriacao())
                .build();
    }
}