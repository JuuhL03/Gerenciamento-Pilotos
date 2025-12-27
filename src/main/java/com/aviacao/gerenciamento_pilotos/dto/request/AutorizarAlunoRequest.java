package com.aviacao.gerenciamento_pilotos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorizarAlunoRequest {

    private LocalDate dataValidade;
    private String observacoes;
}