package org.example.services.adapters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.controllers.dto.responses.SendResponseResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderResponse {
    private SendResponseResult sendResponseResult;
    private String response;
}
