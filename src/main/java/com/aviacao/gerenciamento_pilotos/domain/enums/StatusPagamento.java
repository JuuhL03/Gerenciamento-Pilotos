package com.aviacao.gerenciamento_pilotos.domain.enums;

public enum StatusPagamento {
    PENDENTE,      // Pagamento pendente
    AGUARDANDO,    // Aguardando confirmação
    PAGO,          // Pagamento confirmado
    CANCELADO      // Pagamento cancelado
}