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
package tools.dynamia.cms.site.templates;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
public class TemplateJavaConfig {

	public static final String DEFAULT_TEMPLATE = "CMSCurrentTemplate";

	@Autowired
	private CacheManager cacheManager;

	@Bean
	public SiteTemplateResolver templateResolver() {
		SiteTemplateResolver resolver = new SiteTemplateResolver();
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML");
		resolver.setCacheable(false);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.addDialect(new LayoutDialect());
		engine.addTemplateResolver(templateResolver());

		return engine;
	}

	@Bean
	public ViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setOrder(1);
		viewResolver.setTemplateEngine(templateEngine());
		return viewResolver;
	}

	@Bean
	public ViewResolver zkViewResolver() {
		UrlBasedViewResolver vr = new SiteZKViewResolver();
		vr.setCache(false);
		vr.setOrder(0);
		return vr;
	}

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
