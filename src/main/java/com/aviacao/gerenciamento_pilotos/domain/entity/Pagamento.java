package com.aviacao.gerenciamento_pilotos.domain.entity;

import com.aviacao.gerenciamento_pilotos.domain.enums.StatusPagamento;
import com.aviacao.gerenciamento_pilotos.domain.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoPagamento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(name = "data_geracao", updatable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "comprovante_path", length = 255)
    private String comprovantePath;

    @PrePersist
    protected void onCreate() {
        dataGeracao = LocalDateTime.now();
    }
}