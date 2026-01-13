package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "local_pouso")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("ativo = true")
public class LocalPouso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}