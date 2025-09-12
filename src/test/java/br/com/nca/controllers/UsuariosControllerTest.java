package br.com.nca.controllers;

import static org.junit.jupiter.api.Assertions.fail;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.nca.domain.services.UsuarioService;

@AutoConfigureMockMvc
@WebMvcTest(UsuariosController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes de UsuariosController")
public class UsuariosControllerTest {

    @MockitoBean
    private UsuarioService service;
    
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
    @DisplayName("Deve criar um usuário com sucesso")
    @Test
    public void criarUsuarioComSucesso() {
        fail("Não implementado");
    }
    
    @Order(2)
    @DisplayName("Deve validar todos os campos como obrigatórios")
    @Test
    public void validarCamposObrigatorios() {
        fail("Não implementado");
    }
    
    @Order(3)
    @DisplayName("Deve validar senha forte")
    @Test
    public void validarSenhaForte() {
        fail("Não implementado");
    }
    
    @Order(4)
    @DisplayName("Não deve permitir o cadastro de emails iguais para o usuário")
    @Test
    public void verificarEmailJaCadastrado() {
        fail("Não implementado");
    }
}
