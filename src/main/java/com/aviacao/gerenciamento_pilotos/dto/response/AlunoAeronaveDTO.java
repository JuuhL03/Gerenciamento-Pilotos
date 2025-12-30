package com.aviacao.gerenciamento_pilotos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoAeronaveDTO {
    private Long aeronaveId;
    private String aeronaveNome;
    private Boolean autorizado;
}