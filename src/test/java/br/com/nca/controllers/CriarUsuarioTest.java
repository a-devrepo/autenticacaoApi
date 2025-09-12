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

import br.com.nca.domain.dtos.CriarUsuarioRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Testes de integração para criar um usuário")
public class CriarUsuarioTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Faker faker;
    
    private static String emailUsuario;
    
    private final static String MENSAGEM_ERRO_SENHA_FRACA = "A senha deve ter pelo menos 8 caracteres, com maiúsculas, minúsculas, número, símbolo, sem espaços nem caracteres repetidos.";

    @BeforeEach
    public void setup() {
        faker = new Faker(Locale.of("pt-BR"), new Random(93));
    }

    @Order(1)
    @DisplayName("Deve criar um usuário com sucesso")
    @Test
    public void criarUsuarioComSucesso() {

        try {

            var request = CriarUsuarioRequestDTO.builder()
                    .nome(faker.name().firstName() + " " + faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .senha("@Admin123")
                    .build();

            var result = mockMvc.perform(post("/api/v1/usuarios/criar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn();

            assertEquals(201, result.getResponse().getStatus());
            
            emailUsuario = request.getEmail();

        } catch (Exception exception) {
            fail("Teste falhou: " + exception.getMessage());
        }
    }

    @Order(2)
    @DisplayName("Deve validar todos os campos como obrigatórios")
    @Test
    public void validarCamposObrigatorios() {
        
        try {

            var request = CriarUsuarioRequestDTO.builder()
                    .nome(null)
                    .email(null)
                    .senha(null)
                    .build();

            var result = mockMvc.perform(post("/api/v1/usuarios/criar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn();

            assertEquals(422, result.getResponse().getStatus());

            assertTrue(result.getResponse().getContentAsString().contains("Nome é obrigatório."));
            assertTrue(result.getResponse().getContentAsString().contains("Email é obrigatório."));
            assertTrue(result.getResponse().getContentAsString().contains("Senha é obrigatória."));
            
        } catch (Exception e) {
            fail("Teste falhou: " + e.getMessage());
        }
    }

    @Order(3)
    @DisplayName("Deve validar senha forte")
    @Test
    public void validarSenhaForte() {
      
        try {
            
            var request = CriarUsuarioRequestDTO.builder()
              .nome(faker.name().firstName() + " " + faker.name().lastName())
              .email(faker.internet().emailAddress())
              .senha("cotiinformatica")
              .build();
              
             var result = mockMvc.perform(
                              post("/api/v1/usuarios/criar")
                              .contentType("application/json")
                              .content(objectMapper.writeValueAsString(request)))
                     .andReturn();
                              
              
             assertEquals(422, result.getResponse().getStatus());
                          
             assertTrue(result.getResponse().getContentAsString()
                     .contains(MENSAGEM_ERRO_SENHA_FRACA));
          
        } catch(Exception e) {
              fail("Teste falhou: " + e.getMessage());
          }
    }

    @Order(4)
    @DisplayName("Não deve permitir o cadastro de emails iguais para o usuário")
    @Test
    public void verificarEmailJaCadastrado() {
        
        try {

            var request = CriarUsuarioRequestDTO.builder()
                    .nome(faker.name().firstName() + " " + faker.name().lastName())
                    .email(emailUsuario)
                    .senha("@Admin123")
                    .build();

            var result = mockMvc.perform(post("/api/v1/usuarios/criar")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn();

            assertEquals(409, result.getResponse().getStatus());
            assertTrue(result.getResponse().getContentAsString().contains("O email informado já está cadastrado"));
            
        } catch (Exception exception) {
            fail("Teste falhou: " + exception.getMessage());
        }
    }
}