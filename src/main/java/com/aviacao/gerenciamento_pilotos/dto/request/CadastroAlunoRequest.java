package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroAlunoRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Passaporte é obrigatório")
    private String passaporte;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
}