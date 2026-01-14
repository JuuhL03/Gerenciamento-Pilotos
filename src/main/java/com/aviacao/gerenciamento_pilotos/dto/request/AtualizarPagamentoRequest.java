package com.aviacao.gerenciamento_pilotos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarPagamentoRequest {

    private String comprovanteBase64;
}