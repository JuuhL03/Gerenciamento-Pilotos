package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "aluno_aeronave", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"aluno_id", "aeronave_id"})
})
@Data
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

    @Column(nullable = false)
    private Boolean autorizado = false;

    @CreationTimestamp
    @Column(name = "data_vinculo", updatable = false)
    private LocalDateTime dataVinculo;
}