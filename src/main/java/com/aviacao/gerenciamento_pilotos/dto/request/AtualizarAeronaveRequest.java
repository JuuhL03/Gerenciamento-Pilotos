package com.aviacao.gerenciamento_pilotos.dto.request;

import lombok.Data;

@Data
public class AtualizarAeronaveRequest {
    private String nome;
    private Boolean ativa;
}