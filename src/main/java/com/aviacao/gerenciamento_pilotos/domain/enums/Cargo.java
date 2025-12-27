package com.aviacao.gerenciamento_pilotos.domain.enums;

public enum Cargo {
    ADMIN,      // Administrador - acesso total
    INSTRUTOR,  // Instrutor - pode gerenciar alunos e testes
    OPERADOR    // Operador - apenas leitura
}