package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "teste_id", unique = true, nullable = false)
    private Teste teste;

    @Column(nullable = false)
    private Boolean pago = true;

    @Column(name = "comprovante_nome")
    private String comprovanteNome;

    @Column(name = "comprovante_tipo")
    private String comprovanteTipo;

    @Column(name = "comprovante_tamanho")
    private Long comprovanteTamanho;

    @Lob
    @Column(name = "comprovante_dados", columnDefinition = "LONGBLOB")
    private byte[] comprovanteDados;

    @CreationTimestamp
    @Column(name = "data_pagamento", updatable = false)
    private LocalDateTime dataPagamento;
}