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
public class TemplateJavaConfig {

   
    public static final String DEFAULT_TEMPLATE = "CMSCurrentTemplate";

    @Bean
    public SimpleUrlHandlerMapping templateResourcesMapping() {

        TemplateResourceHandler handler = templateResourcesHandler();
        Map<String, Object> map = new HashMap<>();
        map.put("css/**", handler);
        map.put("styles/**", handler);
        map.put("img/**", handler);
        map.put("images/**", handler);
        map.put("assets/**", handler);
        map.put("js/**", handler);
        map.put("fonts/**", handler);
        map.put("font/**", handler);
        map.put("plugins/**", handler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);

        return mapping;
    }

    @Bean
    public TemplateResourceHandler templateResourcesHandler() {
        return new TemplateResourceHandler();
    }
}
