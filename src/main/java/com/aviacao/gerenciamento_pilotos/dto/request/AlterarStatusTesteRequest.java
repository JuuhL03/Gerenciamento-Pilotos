package com.aviacao.gerenciamento_pilotos.dto.request;

import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlterarStatusTesteRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusTeste status;
}