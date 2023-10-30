package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.controllers.dto.DataItem;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ToItem {
    @NotNull
    @JsonProperty("user_id")
    private Integer userId;
    @Valid
    @NotNull
    @JsonProperty("data")
    private List<DataItem> data;
}
