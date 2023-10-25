package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.controllers.dto.DataItem;

import java.util.List;

@Data
public class ToItem {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("data")
    private List<DataItem> data;
}
