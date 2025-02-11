package com.desafio.contas_pagar_api.service;

import com.desafio.contas_pagar_api.model.Conta;
import com.desafio.contas_pagar_api.model.SituacaoConta;
import com.desafio.contas_pagar_api.repository.ContaRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta criarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Conta atualizarConta(Long id, Conta conta) {
        Optional<Conta> contaOptional = contaRepository.findById(id);
        if (contaOptional.isPresent()) {
            Conta contaExistente = contaOptional.get();
            contaExistente.setDescricao(conta.getDescricao());
            contaExistente.setDataVencimento(conta.getDataVencimento());
            contaExistente.setValor(conta.getValor());
            contaExistente.setSituacao(conta.getSituacao());
            contaExistente.setDataPagamento(conta.getDataPagamento());
            return contaRepository.save(contaExistente);
        }
        throw new RuntimeException("Conta não encontrada para atualização");
    }

    public Conta alterarSituacao(Long id, SituacaoConta situacao) {
        Optional<Conta> contaOptional = contaRepository.findById(id);
        if (contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            conta.setSituacao(situacao);
            return contaRepository.save(conta);
        }
        throw new RuntimeException("Conta não encontrada para alteração de situação");
    }

    public Page<Conta> obterContas(LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable) {
        return contaRepository.findByDataVencimentoBetweenAndDescricaoContaining(startDate, endDate, descricao, pageable);
    }

    public Conta obterContaPorId(Long id) {
        return contaRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    public BigDecimal obterTotalPago(LocalDate startDate, LocalDate endDate) {
        return contaRepository.sumByDataPagamentoBetween(startDate, endDate);
    }

    public void importarContasCsv(MultipartFile file) {
        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
            List<String[]> linhas = csvReader.readAll();

            for (String[] linha : linhas) {
                Conta conta = new Conta();
                conta.setDescricao(linha[0]);
                conta.setDataVencimento(LocalDate.parse(linha[1]));
                conta.setValor(new BigDecimal(linha[2]));
                conta.setSituacao(SituacaoConta.PENDENTE);
                contaRepository.save(conta);
            }
        } catch (CsvException | IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo CSV", e);
        }
    }
}
