package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aeronave")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String nome;

    @Column(length = 30)
    private String categoria;

    @Column(nullable = false)
    private Boolean ativa = true;
}