CREATE TYPE situacao_conta AS ENUM ('PENDENTE', 'PAGO', 'VENCIDO');

CREATE SEQUENCE contas_id_seq
START WITH 1
INCREMENT BY 1;

CREATE TABLE contas (
    id BIGINT PRIMARY KEY DEFAULT nextval('contas_id_seq'::regclass),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10, 2) NOT NULL,
    descricao TEXT NOT NULL,
    situacao situacao_conta NOT NULL
);