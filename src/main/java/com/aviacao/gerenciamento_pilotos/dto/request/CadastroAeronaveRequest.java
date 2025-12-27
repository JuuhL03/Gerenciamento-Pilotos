package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroAeronaveRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String categoria;
}