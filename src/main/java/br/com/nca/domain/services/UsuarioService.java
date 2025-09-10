package br.com.nca.domain.services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import br.com.nca.domain.dtos.AutenticarUsuarioRequestDTO;
import br.com.nca.domain.dtos.AutenticarUsuarioResponseDTO;
import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;
import br.com.nca.domain.dtos.CriarUsuarioResponseDTO;
import br.com.nca.domain.entities.Usuario;
import br.com.nca.domain.exceptions.AccessDeniedException;
import br.com.nca.domain.exceptions.DuplicateEmailException;
import br.com.nca.domain.repositories.UsuarioRepository;
import br.com.nca.helpers.CryptoHelper;
import br.com.nca.helpers.JWTHelper;

@Service
public class UsuarioService {

    private final JWTHelper jwtHelper;

    private UsuarioRepository repository;
    private CryptoHelper cryptoHelper;
    
    public UsuarioService(UsuarioRepository repository, 
            CryptoHelper cryptoHelper, JWTHelper jwtHelper) {
        this.repository = repository;
        this.cryptoHelper = cryptoHelper;
        this.jwtHelper = jwtHelper;
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
        
        var token = jwtHelper.getToken(user.getId());
        
        
        return entityToAntenticateResponse(user, token);
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
    
    private AutenticarUsuarioResponseDTO entityToAntenticateResponse(Usuario usuario, String token) {
        
        var dataHoraAcesso = jwtHelper.getCurrentDate()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        var dataHoraExpiracao = jwtHelper.getExpiration()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        return AutenticarUsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataHoraAcesso(dataHoraAcesso)
                .dataHoraExpiracao(dataHoraExpiracao)
                .token(jwtHelper.getToken(usuario.getId()))
                .build();
    }
}