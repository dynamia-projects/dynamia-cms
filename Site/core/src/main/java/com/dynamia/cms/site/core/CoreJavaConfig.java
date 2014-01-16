/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/**
 *
 * @author mario
 */
@Configuration
@EnableCaching
public class CoreJavaConfig {

    @Bean
    public SimpleUrlHandlerMapping siteResourcesMapping() {

        SiteResourceHandler handler = siteResourcesHandler();

        Map<String, Object> map = new HashMap<>();

        map.put("resources/**", handler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);

        return mapping;
    }

    @Bean
    public SiteResourceHandler siteResourcesHandler() {
        return new SiteResourceHandler();
    }

}
