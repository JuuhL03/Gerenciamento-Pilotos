package com.aviacao.gerenciamento_pilotos.dto.request;

import lombok.Data;

@Data
public class AtualizarLocalPousoRequest {

    private String nome;
    private String imagemUrl;
}