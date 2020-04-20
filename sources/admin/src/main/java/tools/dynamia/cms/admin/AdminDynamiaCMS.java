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
package tools.dynamia.cms.admin;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import tools.dynamia.app.MvcConfiguration;
import tools.dynamia.cms.core.SiteResourceHandler;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mario Serrano Leones
 */

public class AdminDynamiaCMS extends MvcConfiguration {

    public AdminDynamiaCMS() {
        System.err.println("Starting boot application config");
    }

    @Bean
    public LoggingService cmsLoggingService() {
        return new SLF4JLoggingService(AdminDynamiaCMS.class);
    }

    @Bean
    public SimpleUrlHandlerMapping siteResourcesMapping() {
        SiteResourceHandler siteHandler = resourceHandler();
        Map<String, Object> map = new HashMap<>();
        map.put("resources/**", siteHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

        mapping.setUrlMap(map);

        return mapping;
    }

    @Bean
    public SiteResourceHandler resourceHandler() {
        return new SiteResourceHandler();
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
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }

}
