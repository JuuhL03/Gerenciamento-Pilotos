package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CadastrarPagamentoRequest {

    @NotNull(message = "Teste ID é obrigatório")
    private Long testeId;

    @NotBlank(message = "Comprovante em base64 é obrigatório")
    private String comprovanteBase64;

    private String comprovanteNome;

    @NotBlank(message = "Tipo do comprovante é obrigatório")
    private String comprovanteTipo;
}