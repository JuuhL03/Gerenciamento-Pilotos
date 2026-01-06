package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "local_pouso")
@Data
@SQLRestriction("ativo = true")
public class LocalPouso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Lob
    @Column(name = "imagem", columnDefinition = "LONGTEXT")
    private String imagem; // Base64

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "localPouso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlunoLocalPouso> alunoLocaisPouso = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;
}