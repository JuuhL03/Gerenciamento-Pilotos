package com.aviacao.gerenciamento_pilotos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoAlunoRequest {

    private String nome;
    private Integer passaporte;
    private String telefone;
    private Boolean autorizado;
}