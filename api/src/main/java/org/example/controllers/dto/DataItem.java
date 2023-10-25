package org.example.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataItem {
    @JsonProperty("key")
    private String key;
    @JsonProperty("value")
    private String value;
}
