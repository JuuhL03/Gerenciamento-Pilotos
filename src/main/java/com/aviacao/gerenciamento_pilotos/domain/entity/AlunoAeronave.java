package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "aluno_aeronave",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "aeronave_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoAeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "aeronave_id", nullable = false)
    private Aeronave aeronave;

    @Column(name = "data_autorizacao", nullable = false)
    private LocalDateTime dataAutorizacao;

    @PrePersist
    protected void onCreate() {
        if (dataAutorizacao == null) {
            dataAutorizacao = LocalDateTime.now();
        }
    }
}