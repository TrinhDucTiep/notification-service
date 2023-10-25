package org.example.utils;

import org.example.controllers.dto.DataItem;

import java.util.List;

public class TemplateConverter {

    public static String convertTemplate(String form, List<DataItem> data) {
        for(DataItem dataItem: data) {
            form = form.replace("{" + dataItem.getKey() + "}", dataItem.getValue());
        }
        return form;
    }

}
