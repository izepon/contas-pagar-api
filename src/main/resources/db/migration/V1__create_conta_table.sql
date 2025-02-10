CREATE TYPE situacao_conta AS ENUM ('PENDENTE', 'PAGO', 'VENCIDO');

CREATE TABLE contas (
    id BIGINT PRIMARY KEY,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10, 2) NOT NULL,
    descricao TEXT NOT NULL,
    situacao situacao_conta NOT NULL
);