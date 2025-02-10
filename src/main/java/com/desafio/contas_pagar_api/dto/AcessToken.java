package com.desafio.contas_pagar_api.dto;

public class AcessToken {

    private String token;

    public AcessToken(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
