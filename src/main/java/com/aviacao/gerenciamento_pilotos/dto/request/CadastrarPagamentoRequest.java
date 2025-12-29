package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarPagamentoRequest {

    @NotNull(message = "ID do teste é obrigatório")
    private Long testeId;

    @NotBlank(message = "Comprovante em base64 é obrigatório")
    private String comprovanteBase64;

    private String comprovanteNome;  // Nome do arquivo (ex: comprovante.jpg)

    @NotBlank(message = "Tipo do comprovante é obrigatório")
    private String comprovanteTipo;  // mimetype (ex: image/jpeg)
}