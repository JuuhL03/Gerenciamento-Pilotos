package com.aviacao.gerenciamento_pilotos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoLocalPousoDTO {

    private Long alunoId;
    private String alunoNome;
    private Integer alunoPassaporte;

    private Long localPousoId;
    private String localPousoNome;
    private String localPousoImagem;

    private Boolean autorizado;
}