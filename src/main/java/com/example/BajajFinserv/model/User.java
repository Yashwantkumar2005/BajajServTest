package com.example.BajajFinserv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class User {
    private int id;
    private String name;
    @JsonProperty("follows")
    private List<Integer> follows;
}