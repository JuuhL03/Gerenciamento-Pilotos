package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "aluno_local_pouso")
@Data
public class AlunoLocalPouso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "local_pouso_id", nullable = false)
    private LocalPouso localPouso;

    @Column(nullable = false)
    private Boolean autorizado = false;

    @CreationTimestamp
    @Column(name = "data_vinculo", updatable = false)
    private LocalDateTime dataVinculo;
}