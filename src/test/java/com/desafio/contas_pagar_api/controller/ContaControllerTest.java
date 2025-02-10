package com.desafio.contas_pagar_api.controller;

import com.desafio.contas_pagar_api.model.Conta;
import com.desafio.contas_pagar_api.model.SituacaoConta;
import com.desafio.contas_pagar_api.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContaControllerTest {

    private static final Long CONTA_ID = 1L;
    private static final String DESCRICAO = "Teste contas";
    private static final LocalDate DATA_VENCIMENTO = LocalDate.now();
    private static final BigDecimal VALOR = BigDecimal.valueOf(100);
    private static final SituacaoConta SITUACAO = SituacaoConta.PENDENTE;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @Autowired
    private MockMvc mockMvc;

    private Conta conta;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(contaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        conta = instanciaNovaConta();
    }

    private Conta instanciaNovaConta() {
        Conta conta = new Conta();
        conta.setId(CONTA_ID);
        conta.setDescricao(DESCRICAO);
        conta.setDataVencimento(DATA_VENCIMENTO);
        conta.setValor(VALOR);
        conta.setSituacao(SITUACAO);
        return conta;
    }

    @Test
    void criarConta() {
        when(contaService.criarConta(any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.criarConta(conta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void atualizarConta() {
        when(contaService.atualizarConta(eq(CONTA_ID), any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.atualizarConta(CONTA_ID, conta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void alterarSituacao() {
        when(contaService.alterarSituacao(eq(CONTA_ID), any(SituacaoConta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.alterarSituacao(CONTA_ID, SituacaoConta.PAGO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void obterContas() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<Conta> contas = Collections.singletonList(conta);
        Page<Conta> pageContas = new PageImpl<>(contas, pageable, contas.size());

        when(contaService.obterContas(any(LocalDate.class), any(LocalDate.class), anyString(), any(Pageable.class)))
                .thenReturn(pageContas);

        mockMvc.perform(get("/api/contas")
                        .param("startDate", DATA_VENCIMENTO.minusDays(1).toString())
                        .param("endDate", DATA_VENCIMENTO.toString())
                        .param("descricao", DESCRICAO)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].descricao").value(DESCRICAO));
    }

    @Test
    void obterContaPorId() {
        when(contaService.obterContaPorId(CONTA_ID)).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.obterContaPorId(CONTA_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void obterTotalPago() {
        when(contaService.obterTotalPago(any(LocalDate.class), any(LocalDate.class))).thenReturn(BigDecimal.valueOf(500));

        ResponseEntity<BigDecimal> response = contaController.obterTotalPago(DATA_VENCIMENTO, DATA_VENCIMENTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(500), response.getBody());
    }

    @Test
    void importarContasCsv() {
        MultipartFile file = mock(MultipartFile.class);
        doNothing().when(contaService).importarContasCsv(file);

        ResponseEntity<String> response = contaController.importarContasCsv(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contas importadas com sucesso", response.getBody());
    }
}
