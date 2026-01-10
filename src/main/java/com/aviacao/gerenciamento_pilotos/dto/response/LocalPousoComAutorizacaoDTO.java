package com.aviacao.gerenciamento_pilotos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalPousoComAutorizacaoDTO {

    private Long id;
    private String nome;
    private String imagem;
    private Boolean ativo;
    private Boolean autorizado;
}