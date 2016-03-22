/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.core;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.app.VelocityEngine;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
@EnableCaching

public class CoreJavaConfig {

    @Bean
    public SimpleUrlHandlerMapping siteResourcesMapping() {

        SiteResourceHandler siteHandler = siteResourcesHandler();
        ResourceHttpRequestHandler modulesHandler = modulesResourcesHandler();

        Map<String, Object> map = new HashMap<>();

        map.put("resources/**", siteHandler);
        map.put("$mods/**/*.css", modulesHandler);
        map.put("$mods/**/*.js", modulesHandler);
        map.put("$mods/**/*.svg", modulesHandler);
        map.put("$mods/**/*.eot", modulesHandler);
        map.put("$mods/**/*.ttf", modulesHandler);
        map.put("$mods/**/*.woff", modulesHandler);
        map.put("$mods/**/*.png", modulesHandler);
        map.put("$mods/**/*.jpg", modulesHandler);
        map.put("$mods/**/*.gif", modulesHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);

        return mapping;
    }

    @Bean
    public SiteResourceHandler siteResourcesHandler() {
        return new SiteResourceHandler();
    }

    @Bean
    public ModuleResourcesHandler modulesResourcesHandler() {
        return new ModuleResourcesHandler();
    }

    @Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine engine = new VelocityEngine();

        return engine;
    }

    @Bean
    public MustacheFactory mustacheFactory() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf;
    }

}
