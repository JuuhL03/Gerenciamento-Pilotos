package com.aviacao.gerenciamento_pilotos.domain.entity;

import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "teste")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(nullable = false, length = 20)
    private StatusTeste status = StatusTeste.EM_ANDAMENTO;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;

    @OneToOne(mappedBy = "teste", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
}