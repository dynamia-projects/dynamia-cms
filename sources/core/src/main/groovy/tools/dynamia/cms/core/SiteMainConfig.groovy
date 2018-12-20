/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.core

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.apache.velocity.app.VelocityEngine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler

/**
 *
 * @author Mario Serrano Leones
 */

@Configuration
@EnableAsync
@EnableWebMvc
@EnableScheduling
class SiteMainConfig extends WebMvcConfigurerAdapter {

    @Bean
    SimpleUrlHandlerMapping siteResourcesMapping() {

        SiteResourceHandler siteHandler = siteResourcesHandler()
        ResourceHttpRequestHandler modulesHandler = modulesResourcesHandler()
        SiteStaticResourceHandler staticHandler = siteStaticResourceHandler()

        Map<String, Object> map = new HashMap<>()

        map.put("resources/**", siteHandler)
        map.put("static/**", staticHandler)
        map.put('$mods/**/*.css', modulesHandler)
        map.put('$mods/**/*.js', modulesHandler)
        map.put('$mods/**/*.json', modulesHandler)
        map.put('$mods/**/*.svg', modulesHandler)
        map.put('$mods/**/*.eot', modulesHandler)
        map.put('$mods/**/*.ttf', modulesHandler)
        map.put('$mods/**/*.woff', modulesHandler)
        map.put('$mods/**/*.png', modulesHandler)
        map.put('$mods/**/*.jpg', modulesHandler)
        map.put('$mods/**/*.gif', modulesHandler)
        map.put('$mods/**/*.ini', modulesHandler)
        map.put('$mods/**/*.yml', modulesHandler)
        map.put('$mods/**/*.xml', modulesHandler)
        map.put('$mods/**/*.txt', modulesHandler)
        map.put('$mods/**/*.md', modulesHandler)

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping()

        mapping.urlMap = map

        return mapping
    }

    @Bean
    SiteResourceHandler siteResourcesHandler() {
        return new SiteResourceHandler()
    }

    @Bean
    SiteStaticResourceHandler siteStaticResourceHandler() {
        return new SiteStaticResourceHandler()
    }

    @Bean
    ModuleResourcesHandler modulesResourcesHandler() {
        return new ModuleResourcesHandler()
    }

    @Bean
    VelocityEngine velocityEngine() {
        VelocityEngine engine = new VelocityEngine()

        return engine
    }

    @Bean
    MustacheFactory mustacheFactory() {
        MustacheFactory mf = new DefaultMustacheFactory()
        return mf
    }

    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor()
    }

    @Bean
    SiteHandleInterceptor siteHandleInterceptor() {
        return new SiteHandleInterceptor()
    }

    @Override
    void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(siteHandleInterceptor()).addPathPatterns("/**").excludePathPatterns("/resources/**",
                "/css/**", "/styles/**", "/js/**", "/fonts/**", "/font/**", "/assets/**", "/images/**", "/img/**")
    }

}
