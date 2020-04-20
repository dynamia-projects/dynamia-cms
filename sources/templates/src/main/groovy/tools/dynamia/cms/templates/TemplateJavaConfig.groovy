/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.templates

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.accept.ContentNegotiationManager
import org.springframework.web.accept.FixedContentNegotiationStrategy
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.thymeleaf.templatemode.TemplateMode
import tools.dynamia.app.ApplicationInfo

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
class TemplateJavaConfig {

    public static final String DEFAULT_TEMPLATE = "CMSCurrentTemplate"

    @Autowired
    private ApplicationInfo applicationInfo

    @Bean
    ViewResolver contentNegotiatingViewResolver() {

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver()

        Map<String, MediaType> mediaTypes = new HashMap<>()
        mediaTypes["html"] = MediaType.TEXT_HTML
        mediaTypes["json"] = MediaType.APPLICATION_JSON

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
        map["css/**"] = handler
        map["styles/**"] = handler
        map["img/**"] = handler
        map["images/**"] = handler
        map["assets/**"] = handler
        map["js/**"] = handler
        map["fonts/**"] = handler
        map["font/**"] = handler
        map["plugins/**"] = handler

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping()
        mapping.urlMap = map

        return mapping
    }

    @Bean
    TemplateResourceHandler templateResourcesHandler() {
        return new TemplateResourceHandler()
    }



}
