/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/**
 *
 * @author mario
 */
@Configuration
public class Config {

    public static final String TEMPLATES_LOCATION = "CMSTemplatesLocation";
    public static final String CURRENT_TEMPLATE = "CMSCurrentTemplate";

    @Bean
    public SimpleUrlHandlerMapping resourcesMapping() {

        TemplateResourceHandler handler = resourcesHandler();
        Map<String, Object> map = new HashMap<>();
        map.put("/css/**", handler);
        map.put("/img/**", handler);
        map.put("/js/**", handler);
        map.put("/fonts/**", handler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);

        return mapping;
    }

    @Bean
    public TemplateResourceHandler resourcesHandler() {
        return new TemplateResourceHandler();
    }
}
