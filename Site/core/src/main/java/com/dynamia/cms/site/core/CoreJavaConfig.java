/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 *
 * @author mario
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

}
