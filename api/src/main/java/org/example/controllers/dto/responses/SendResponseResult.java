package org.example.controllers.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendResponseResult {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("status")
    private Status status;
}
