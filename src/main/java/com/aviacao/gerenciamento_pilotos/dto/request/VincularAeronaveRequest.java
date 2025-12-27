package com.aviacao.gerenciamento_pilotos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VincularAeronaveRequest {

    @NotNull(message = "ID da aeronave é obrigatório")
    private Long aeronaveId;
}