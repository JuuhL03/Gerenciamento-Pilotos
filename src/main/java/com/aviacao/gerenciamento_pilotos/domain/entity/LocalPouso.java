package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "local_pouso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalPouso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "foto_nome")
    private String fotoNome;

    @Column(name = "foto_tipo")
    private String fotoTipo;  // mimetype (image/jpeg, image/png, etc)

    @Column(name = "foto_tamanho")
    private Long fotoTamanho;  // tamanho em bytes

    @Lob
    @Column(name = "foto_dados", columnDefinition = "LONGBLOB")
    private byte[] fotoDados;  // arquivo em base64/bytes

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}