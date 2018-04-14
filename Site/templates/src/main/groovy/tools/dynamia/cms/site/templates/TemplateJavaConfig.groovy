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
package tools.dynamia.cms.site.templates

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.accept.ContentNegotiationManager
import org.springframework.web.accept.FixedContentNegotiationStrategy
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver
import org.thymeleaf.spring4.SpringTemplateEngine
import org.thymeleaf.spring4.view.ThymeleafViewResolver
import org.thymeleaf.templatemode.TemplateMode

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
class TemplateJavaConfig {

	public static final String DEFAULT_TEMPLATE = "CMSCurrentTemplate"

    @Bean
    ViewResolver contentNegotiatingViewResolver() {

		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver()

        Map<String, MediaType> mediaTypes = new HashMap<>()
        mediaTypes.put("html", MediaType.TEXT_HTML)
        mediaTypes.put("json", MediaType.APPLICATION_JSON)

        ContentNegotiationManager manager = new ContentNegotiationManager(
				new PathExtensionContentNegotiationStrategy(mediaTypes),
				new FixedContentNegotiationStrategy(MediaType.TEXT_HTML)
				)
        resolver.contentNegotiationManager = manager

        // Define all possible view resolvers
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>()
        resolvers.add(jsonViewResolver())
        resolvers.add(thymeleafViewResolver())

        resolver.viewResolvers = resolvers
        return resolver
    }

	@Bean
    SiteTemplateResolver templateResolver() {
		SiteTemplateResolver resolver = new SiteTemplateResolver()
        resolver.suffix = ".html"
        resolver.templateMode = TemplateMode.HTML
        resolver.characterEncoding = "UTF-8"
        resolver.cacheable = false
        return resolver
    }

	@Bean
    SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine()

        engine.addDialect(new LayoutDialect())
        engine.addTemplateResolver(templateResolver())

        return engine
    }


    ViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver()
        viewResolver.order = 1
        viewResolver.templateEngine = templateEngine()
        viewResolver.characterEncoding = "UTF-8"

        return viewResolver
    }

    ViewResolver jsonViewResolver() {
		JsonViewResolver json = new JsonViewResolver()
        json.order = 2
        return json
    }

	@Bean
    SimpleUrlHandlerMapping templateResourcesMapping() {

		TemplateResourceHandler handler = templateResourcesHandler()
        Map<String, Object> map = new HashMap<>()
        map.put("css/**", handler)
        map.put("styles/**", handler)
        map.put("img/**", handler)
        map.put("images/**", handler)
        map.put("assets/**", handler)
        map.put("js/**", handler)
        map.put("fonts/**", handler)
        map.put("font/**", handler)
        map.put("plugins/**", handler)

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping()
        mapping.urlMap = map

        return mapping
    }

	@Bean
    TemplateResourceHandler templateResourcesHandler() {
		return new TemplateResourceHandler()
    }

}
