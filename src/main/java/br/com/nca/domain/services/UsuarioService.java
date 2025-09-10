package br.com.nca.domain.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.nca.domain.dtos.AutenticarUsuarioRequestDTO;
import br.com.nca.domain.dtos.AutenticarUsuarioResponseDTO;
import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;
import br.com.nca.domain.dtos.CriarUsuarioResponseDTO;
import br.com.nca.domain.entities.Usuario;
import br.com.nca.domain.exceptions.DuplicateEmailException;
import br.com.nca.domain.exceptions.AccessDeniedException;
import br.com.nca.domain.repositories.UsuarioRepository;
import br.com.nca.helpers.CryptoHelper;

@Service
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
        
        return entityToCreateResponse(usuario);
    }
    
    public AutenticarUsuarioResponseDTO autenticarUsuario(AutenticarUsuarioRequestDTO requestDto) {
        
        
        var user = repository
                .findUserByEmailPassword(requestDto.getEmail(),
                        cryptoHelper.getSha256(requestDto.getSenha()));
        
        if(user == null) {
            throw new AccessDeniedException();
        }
        
        return entityToAntenticateResponse(user);
    }
    
    private Usuario requestToEntity(CriarUsuarioRequestDTO dto) {
        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(cryptoHelper.getSha256(dto.getSenha()))
                .dataCriacao(LocalDateTime.now())
                .build();
    }
    
    private CriarUsuarioResponseDTO entityToCreateResponse(Usuario usuario) {
        return CriarUsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataHoraCriacao(usuario.getDataCriacao())
                .build();
    }
    
    private AutenticarUsuarioResponseDTO entityToAntenticateResponse(Usuario usuario) {
        
        var dataHoraAcesso = LocalDateTime.now();
        
        return AutenticarUsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataHoraAcesso(dataHoraAcesso)
                .dataHoraExpiracao(dataHoraAcesso.plusHours(1))
                .build();
    }
}