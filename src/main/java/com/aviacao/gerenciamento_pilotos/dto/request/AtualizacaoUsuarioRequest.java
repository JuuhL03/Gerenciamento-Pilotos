package com.aviacao.gerenciamento_pilotos.dto.request;

import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoUsuarioRequest {

    private String passaporte;
    private String nome;
    private String telefone;
    private Cargo cargo;
    private Boolean ativo;
    private String novaSenha;
}