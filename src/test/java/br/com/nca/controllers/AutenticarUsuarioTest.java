package br.com.nca.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Locale;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.nca.domain.dtos.AutenticarUsuarioRequestDTO;
import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Testes de integração para autenticar um usuário")
public class AutenticarUsuarioTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
    private Faker faker;
    
    @BeforeEach
    public void setup() {
        faker = new Faker(Locale.of("pt-BR"), new Random(93));
    }
    
    @Order(1)
    @DisplayName("Deve autenticar um usuário com sucesso")
    @Test
    public void deveAutenticarUsuarioComSucesso() {

        try {

            var email = faker.internet().emailAddress();
            var senha = "@Admin123";
            
            var requestCriar = CriarUsuarioRequestDTO.builder()
                    .nome(faker.name().firstName() + " " + faker.name().lastName())
                    .email(email)
                    .senha(senha)
                    .build();
            
            var resultCriar = mockMvc.perform(post("/api/v1/usuarios/criar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(requestCriar)))
                    .andReturn();
            
            var requestAutenticar = AutenticarUsuarioRequestDTO.builder()
                    .email(email)
                    .senha(senha)
                    .build();
            
            var resultAutenticar = mockMvc.perform(post("/api/v1/usuarios/autenticar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(requestAutenticar)))
                    .andReturn();
            
            assertEquals(201,resultCriar.getResponse().getStatus());
            assertEquals(200,resultAutenticar.getResponse().getStatus());
            
        } catch (Exception exception) {
            fail("Teste falhou: " + exception.getMessage());
        }
    }
    
    @Order(2)
    @DisplayName("Deve validar todos os campos como obrigatórios")
    @Test
    public void validarCamposObrigatorios() {

        try {
            
            var request = AutenticarUsuarioRequestDTO
                    .builder()
                    .email(null)
                    .senha(null)
                    .build();
            
            var resultAutenticar = mockMvc.perform(post("/api/v1/usuarios/autenticar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn();
            
            assertEquals(422,resultAutenticar.getResponse().getStatus());
            assertTrue(resultAutenticar.getResponse().getContentAsString().contains("Email é obrigatório."));
            assertTrue(resultAutenticar.getResponse().getContentAsString().contains("Senha é obrigatória."));
            
        } catch (Exception exception) {
            fail("Teste falhou: " + exception.getMessage());
        }
    }
    
    @Order(3)
    @DisplayName("Deve retornar acesso negado para usuário inválido")
    @Test
    public void validarAcessoNegado() {

        try {
            
            var faker = new Faker();
            
            var request = AutenticarUsuarioRequestDTO
                    .builder()
                    .email(faker.internet().emailAddress())
                    .senha("@Admin123")
                    .build();
            
            var resultAutenticar = mockMvc.perform(post("/api/v1/usuarios/autenticar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn();
            
            assertEquals(401,resultAutenticar.getResponse().getStatus());
            assertTrue(resultAutenticar.getResponse().getContentAsString().contains("Credenciais inválidas"));
            
        } catch (Exception exception) {
            fail("Teste falhou: " + exception.getMessage());
        }
    }
}