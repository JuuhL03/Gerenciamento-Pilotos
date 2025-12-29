package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "teste_id", unique = true, nullable = false)
    private Teste teste;

    @Column(nullable = false)
    private Boolean pago = false;

    @Column(name = "comprovante_nome")
    private String comprovanteNome;

    @Column(name = "comprovante_tipo")
    private String comprovanteTipo;  // mimetype (image/jpeg, image/png, etc)

    @Column(name = "comprovante_tamanho")
    private Long comprovanteTamanho;  // tamanho em bytes

    @Lob
    @Column(name = "comprovante_dados", columnDefinition = "LONGBLOB")
    private byte[] comprovanteDados;  // imagem em bytes

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @PrePersist
    protected void onCreate() {
        if (dataPagamento == null) {
            dataPagamento = LocalDateTime.now();
        }
    }
}