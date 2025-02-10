package com.desafio.contas_pagar_api.repository;

import com.desafio.contas_pagar_api.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByDataVencimentoBetweenAndDescricaoContaining(LocalDate startDate, LocalDate endDate, String descricao);

    Optional<Conta> findById(Long id);

    BigDecimal sumByDataPagamentoBetween(LocalDate startDate, LocalDate endDate);
}
