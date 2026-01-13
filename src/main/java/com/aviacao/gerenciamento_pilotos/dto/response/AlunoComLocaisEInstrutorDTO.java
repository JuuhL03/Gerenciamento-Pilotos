package com.aviacao.gerenciamento_pilotos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoComLocaisEInstrutorDTO {

    private Long alunoId;
    private String alunoNome;
    private Integer alunoPassaporte;
    private Long instrutorId;
    private String instrutorNome;
    private List<LocalPousoSimplesDTO> locaisPouso;
}