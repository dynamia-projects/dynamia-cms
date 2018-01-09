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
package tools.dynamia.cms.site.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
@EnableAsync
@EnableWebMvc
@ComponentScan(basePackages = "tools.dynamia", excludeFilters = {
		@ComponentScan.Filter(classes = Configuration.class, type = FilterType.ANNOTATION),
		@ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION) })
@ComponentScan(basePackages = { "com.dynamia" })
public class SiteMainConfig extends WebMvcConfigurerAdapter {

	@Bean
	public SimpleUrlHandlerMapping siteResourcesMapping() {

		SiteResourceHandler siteHandler = siteResourcesHandler();
		ResourceHttpRequestHandler modulesHandler = modulesResourcesHandler();
		SiteStaticResourceHandler staticHandler = siteStaticResourceHandler();

		Map<String, Object> map = new HashMap<>();

		map.put("resources/**", siteHandler);
		map.put("static/**", staticHandler);
		map.put("$mods/**/*.css", modulesHandler);
		map.put("$mods/**/*.js", modulesHandler);
		map.put("$mods/**/*.json", modulesHandler);
		map.put("$mods/**/*.svg", modulesHandler);
		map.put("$mods/**/*.eot", modulesHandler);
		map.put("$mods/**/*.ttf", modulesHandler);
		map.put("$mods/**/*.woff", modulesHandler);
		map.put("$mods/**/*.png", modulesHandler);
		map.put("$mods/**/*.jpg", modulesHandler);
		map.put("$mods/**/*.gif", modulesHandler);
		map.put("$mods/**/*.ini", modulesHandler);
		map.put("$mods/**/*.yml", modulesHandler);
		map.put("$mods/**/*.xml", modulesHandler);
		map.put("$mods/**/*.txt", modulesHandler);
		map.put("$mods/**/*.md", modulesHandler);

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

		mapping.setUrlMap(map);

		return mapping;
	}

	@Bean
	public SiteResourceHandler siteResourcesHandler() {
		return new SiteResourceHandler();
	}

	@Bean
	public SiteStaticResourceHandler siteStaticResourceHandler() {
		return new SiteStaticResourceHandler();
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

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	public SiteHandleInterceptor siteHandleInterceptor() {
		return new SiteHandleInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(siteHandleInterceptor()).addPathPatterns("/**").excludePathPatterns("/resources/**",
				"/css/**", "/styles/**", "/js/**", "/fonts/**", "/font/**", "/assets/**", "/images/**", "/img/**");
	}

}
