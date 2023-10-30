package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.example.controllers.dto.DataItem;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TemplateConverter {
    private final TemplateEngine templateEngine;

    public String convertTemplate(String form, List<DataItem> data) {
        Context context = new Context();
        for(DataItem dataItem: data) {
            context.setVariable(dataItem.getKey(), dataItem.getValue());
        }

        return templateEngine.process(form, context);
    }

}
