package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AtualizarPagamentoRequest {

    private BigDecimal valor;

    @NotBlank(message = "Comprovante em base64 é obrigatório")
    private String comprovanteBase64;

    private String comprovanteNome;

    @NotBlank(message = "Tipo do comprovante é obrigatório")
    private String comprovanteTipo;
}