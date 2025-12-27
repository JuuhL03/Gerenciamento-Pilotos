package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarLocalPousoRequest {

    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;

    @NotBlank(message = "Nome do local é obrigatório")
    private String nome;

    private String fotoNome;
    private String fotoTipo;
    private Long fotoTamanho;

    @NotBlank(message = "Foto em base64 é obrigatória")
    private String fotoBase64;
}