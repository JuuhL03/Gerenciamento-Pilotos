package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtribuirAvaliadorRequest {

    @NotNull(message = "ID do avaliador é obrigatório")
    private Long avaliadorId;
}