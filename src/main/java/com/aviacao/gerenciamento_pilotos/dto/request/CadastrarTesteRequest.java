package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarTesteRequest {

    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;

    private Long avaliadorId;
}