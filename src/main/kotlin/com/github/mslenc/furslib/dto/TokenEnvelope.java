package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenEnvelope {
    private String token;

    public TokenEnvelope() {

    }

    public TokenEnvelope(String token) {
        setToken(token);
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }
}
