package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarPagamentoRequest {

    @NotNull(message = "Teste ID é obrigatório")
    private Long testeId;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    private String comprovanteBase64;
}