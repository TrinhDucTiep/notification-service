package org.example.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
    public static Pageable generate(Integer page, Integer size, String sort) {
        if (page == null)
            page = 0;
        if(size == null)
            size = 5;

        if(sort == null || sort.isBlank()) {
            return PageRequest.of(page, size);
        }

        sort = sort.split(",")[0];

        if(sort.charAt(0) == '-') {
            return PageRequest.of(page, size, Sort.by(sort.substring(1)).descending());
        }

        return PageRequest.of(page, size, Sort.by(sort).ascending());
    }

    static String snakeToCamel(String str)
    {
        // Convert to StringBuilder
        StringBuilder builder
                = new StringBuilder(str);

        // Traverse the string character by
        // character and remove underscore
        // and capitalize next letter
        for (int i = 0; i < builder.length(); i++) {

            // Check char is underscore
            if (builder.charAt(i) == '_') {

                builder.deleteCharAt(i);
                builder.replace(
                        i, i + 1,
                        String.valueOf(
                                Character.toUpperCase(
                                        builder.charAt(i))));
            }
        }

        // Return in String type
        return builder.toString();
    }
}
