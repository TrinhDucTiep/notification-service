package org.example.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DataItem {
    @NotNull
    @NotBlank
    @JsonProperty("key")
    private String key;
    @NotNull
    @NotBlank
    @JsonProperty("value")
    private String value;
}
