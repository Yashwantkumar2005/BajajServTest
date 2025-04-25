package com.example.BajajFinserv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class webHookResponse {
    @JsonProperty("webhook")
    private String webhook;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("data")
    private Map<String, Object> data;
}