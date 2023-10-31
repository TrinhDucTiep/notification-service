package org.example.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.controllers.dto.DataItem;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class TemplateConverter {
    private final TemplateEngine templateEngine;

    public String convertTemplate(String form, List<DataItem> data) {
        Context context = new Context();
        for(DataItem dataItem : data) {
            context.setVariable(dataItem.getKey(), dataItem.getValue());
        }

        return templateEngine.process(form, context);
    }

    public static boolean isDataValid(List<DataItem> data, Set<String> templateVariables) {
        // convert data to hashmap
        Map<String, String> jsonDataMap = new HashMap<>();
        for (DataItem item : data) {
            jsonDataMap.put(item.getKey(), item.getValue());
        }

        // check
        for (String variable : templateVariables) {
            if (!jsonDataMap.containsKey(variable)) {
                return false;
            }
        }

        return true;
    }

    public static Set<String> extractVariables(String titleTemplate, String contentTemplate) {
        Set<String> variables = new HashSet<>();
        Set<String> addedVariables = new HashSet<>();

        extractVariablesFromTemplate(titleTemplate, variables, addedVariables);
        extractVariablesFromTemplate(contentTemplate, variables, addedVariables);

        return variables;
    }

    private static void extractVariablesFromTemplate(String template, Set<String> variables, Set<String> addedVariables) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String variable = matcher.group(1);
            if (!addedVariables.contains(variable)) {
                variables.add(variable);
                addedVariables.add(variable);
            }
        }
    }

}
