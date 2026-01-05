package com.aviacao.gerenciamento_pilotos.domain.entity;

import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("ativo = true")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String passaporte;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(unique = true, nullable = false, length = 50)
    private String login;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Cargo cargo;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataUltimaAtualizacao;

    @ManyToOne
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataUltimaAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataUltimaAtualizacao = LocalDateTime.now();
    }
}