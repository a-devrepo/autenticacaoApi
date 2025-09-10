package br.com.nca.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import br.com.nca.domain.dtos.AutenticarUsuarioRequestDTO;
import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;
import br.com.nca.domain.entities.Usuario;
import br.com.nca.domain.exceptions.AccessDeniedException;
import br.com.nca.domain.exceptions.DuplicateEmailException;
import br.com.nca.domain.repositories.UsuarioRepository;
import br.com.nca.domain.services.UsuarioService;
import br.com.nca.helpers.CryptoHelper;
import br.com.nca.helpers.JWTHelper;

@DisplayName("Testes de UsuarioService")
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    private UsuarioService service;
    
    @Mock
    private UsuarioRepository repository;
    
    @Mock
    private JWTHelper jwtHelper;
    
    @Mock
    private CryptoHelper cryptoHelper;
    
    private Faker faker;
    
    @BeforeEach
    public void setup() {
        faker = new Faker(Locale.of("pt-BR"),new Random(93));
        service = new UsuarioService(repository, cryptoHelper, jwtHelper);
    }

    @DisplayName("Deve criar usuário com sucesso")
    @Test
    public void deveCriarUsuarioComSucesso() {
        
        var senha = faker.lorem().characters(8, 16, true,true);
        var senhaCriptografada = faker.lorem().characters(8, 16, true,true);
                
        var requestDTO = CriarUsuarioRequestDTO
        .builder()
        .nome(faker.name().firstName())
        .senha(senha)
        .email(faker.internet().emailAddress())
        .build();
        
        when(repository.findByUserEmail(requestDTO.getEmail())).thenReturn(null);
        when(cryptoHelper.getSha256(senha)).thenReturn(senhaCriptografada);
        
        var usuarioSalvo = Usuario.builder()
        .id(UUID.randomUUID())
        .nome(requestDTO.getNome())
        .email(requestDTO.getEmail())
        .senha(senhaCriptografada)
        .dataCriacao(LocalDateTime.now())
        .build();
        
        when(repository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        
        var responseDTO = service.criarUsuario(requestDTO);
        
        assertThat(responseDTO.getEmail()).isEqualTo(requestDTO.getEmail());
        assertThat(responseDTO.getNome()).isEqualTo(requestDTO.getNome());
    }
    
    @DisplayName("Quando o email for duplicado deve lançar DuplicateEmailException")
    @Test
    public void deveLancarExceptionQuandoEmailDuplicado() {
        
        var senha = faker.lorem().characters(8, 16, true,true);
        
        var requestDTO = CriarUsuarioRequestDTO
        .builder()
        .nome(faker.name().firstName())
        .senha(senha)
        .email(faker.internet().emailAddress())
        .build();
        
        when(repository.findByUserEmail(requestDTO.getEmail())).thenReturn(new Usuario());
        
        assertThatThrownBy(() -> service
                .criarUsuario(requestDTO))
        .isInstanceOf(DuplicateEmailException.class);
    }
    
    @DisplayName("Quando usuário é cadastrado deve retornar usuário autenticado")
    @Test
    public void deveAutenticarUsuarioQuandoJaCadastrado() {
        
        var senha = faker.lorem().characters(8, 16, true,true);
        var senhaCriptografada = faker.lorem().characters(8, 16, true,true);
        var fakeToken = faker.lorem().characters(8, 50, true,true);
                
        var requestDTO = AutenticarUsuarioRequestDTO
        .builder()
        .senha(senha)
        .email(faker.internet().emailAddress())
        .build();
        
        when(cryptoHelper.getSha256(senha)).thenReturn(senhaCriptografada);
        
        var usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nome(faker.name().fullName())
                .email(requestDTO.getEmail())
                .senha(senhaCriptografada)
                .dataCriacao(LocalDateTime.now())
                .build();
        
        
        when(repository.findUserByEmailPassword(requestDTO.getEmail(),senhaCriptografada))
        .thenReturn(usuario);
        
        var currentDate = new Date();
        var expiration = new Date(System.currentTimeMillis() * 3600000);
        
        when(jwtHelper.getToken(usuario.getId())).thenReturn(fakeToken);
        when(jwtHelper.getCurrentDate()).thenReturn(currentDate);
        when(jwtHelper.getExpiration()).thenReturn(expiration);
        
        var responseDTO = service.autenticarUsuario(requestDTO);
        
        assertThat(responseDTO.getToken()).isEqualTo(fakeToken);
        assertThat(responseDTO.getEmail()).isEqualTo(requestDTO.getEmail());
    }
    
    @DisplayName("Quando usuário não é cadastrado deve lançar AccessDeniedException")
    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoCadastrado() {
        
        var senha = faker.lorem().characters(8, 16, true,true);
        var senhaCriptografada = faker.lorem().characters(8, 16, true,true);
                
        var requestDTO = AutenticarUsuarioRequestDTO
        .builder()
        .senha(senha)
        .email(faker.internet().emailAddress())
        .build();
        
        when(cryptoHelper.getSha256(senha)).thenReturn(senhaCriptografada);
        
        
        
        when(repository.findUserByEmailPassword(requestDTO.getEmail(),senhaCriptografada))
        .thenReturn(null);
        
        assertThatThrownBy(() -> service
                .autenticarUsuario(requestDTO))
        .isInstanceOf(AccessDeniedException.class);
    }
}

