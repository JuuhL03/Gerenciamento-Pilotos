package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CadastrarAeronaveRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
}