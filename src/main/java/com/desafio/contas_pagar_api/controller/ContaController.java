package com.desafio.contas_pagar_api.controller;

import com.desafio.contas_pagar_api.model.Conta;
import com.desafio.contas_pagar_api.model.SituacaoConta;
import com.desafio.contas_pagar_api.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        Conta contaCriada = contaService.criarConta(conta);
        return new ResponseEntity<>(contaCriada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        Conta contaAtualizada = contaService.atualizarConta(id, conta);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PutMapping("/{id}/situacao")
    public ResponseEntity<Conta> alterarSituacao(@PathVariable Long id, @RequestParam SituacaoConta situacao) {
        Conta contaAlterada = contaService.alterarSituacao(id, situacao);
        return ResponseEntity.ok(contaAlterada);
    }

    @GetMapping
    public ResponseEntity<Page<Conta>> obterContas(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String descricao,
            Pageable pageable) {
        Page<Conta> contas = contaService.obterContas(startDate, endDate, descricao, pageable);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> obterContaPorId(@PathVariable Long id) {
        Conta conta = contaService.obterContaPorId(id);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> obterTotalPago(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        BigDecimal totalPago = contaService.obterTotalPago(startDate, endDate);
        return ResponseEntity.ok(totalPago);
    }

    @PostMapping("/importar")
    public ResponseEntity<String> importarContasCsv(@RequestParam("file") MultipartFile file) {
        contaService.importarContasCsv(file);
        return ResponseEntity.ok("Contas importadas com sucesso");
    }
}
