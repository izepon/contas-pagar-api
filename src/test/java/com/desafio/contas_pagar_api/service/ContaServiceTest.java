package com.desafio.contas_pagar_api.service;


import com.desafio.contas_pagar_api.model.Conta;
import com.desafio.contas_pagar_api.model.SituacaoConta;
import com.desafio.contas_pagar_api.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    private static final Long ID = 1L;
    private static final String DESCRICAO = "Conta de Teste";
    private static final LocalDate DATA_VENCIMENTO = LocalDate.of(2025, 2, 10);
    private static final BigDecimal VALOR = new BigDecimal("100.00");
    private static final SituacaoConta SITUACAO = SituacaoConta.PENDENTE;

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = instanciaConta();
    }

    private Conta instanciaConta() {
        Conta c = new Conta();
        c.setId(ID);
        c.setDescricao(DESCRICAO);
        c.setDataVencimento(DATA_VENCIMENTO);
        c.setValor(VALOR);
        c.setSituacao(SITUACAO);
        return c;
    }

    @Test
    void criarConta_DeveSalvarConta() {
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.criarConta(conta);

        assertNotNull(result);
        assertEquals(DESCRICAO, result.getDescricao());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void atualizarConta_DeveAtualizarQuandoContaExiste() {
        when(contaRepository.findById(ID)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.atualizarConta(ID, conta);

        assertEquals(DESCRICAO, result.getDescricao());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void atualizarConta_DeveLancarExcecao_QuandoContaNaoExiste() {
        when(contaRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.atualizarConta(ID, conta));
    }

    @Test
    void obterContaPorId_DeveRetornarConta_QuandoExiste() {
        when(contaRepository.findById(ID)).thenReturn(Optional.of(conta));

        Conta result = contaService.obterContaPorId(ID);

        assertNotNull(result);
        assertEquals(DESCRICAO, result.getDescricao());
    }

    @Test
    void obterContaPorId_DeveLancarExcecao_QuandoNaoExiste() {
        when(contaRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.obterContaPorId(ID));
    }
}

