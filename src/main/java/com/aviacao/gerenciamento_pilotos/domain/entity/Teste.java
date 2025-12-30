package com.aviacao.gerenciamento_pilotos.domain.entity;

import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "teste")
@Data
public class Teste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "avaliador_id")
    private Usuario avaliador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTeste status;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToOne(mappedBy = "teste", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;
}