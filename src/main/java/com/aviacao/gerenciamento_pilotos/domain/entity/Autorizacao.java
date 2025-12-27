package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "autorizacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autorizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "data_autorizacao", nullable = false)
    private LocalDateTime dataAutorizacao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(nullable = false)
    private Boolean ativa = true;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @PrePersist
    protected void onCreate() {
        dataAutorizacao = LocalDateTime.now();
    }
}