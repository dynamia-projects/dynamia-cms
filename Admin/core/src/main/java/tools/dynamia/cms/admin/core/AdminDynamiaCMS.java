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
package tools.dynamia.cms.admin.core;

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
import tools.dynamia.app.MvcConfiguration;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

/**
 * @author Mario Serrano Leones
 */
@Configuration
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "tools.dynamia", excludeFilters = {
        @ComponentScan.Filter(classes = Configuration.class, type = FilterType.ANNOTATION),
        @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION)})
@ComponentScan(basePackages = {"tools.dynamia.app", "tools.dynamia.zk", "com.dynamia.icons"})
@Order(1)
public class AdminDynamiaCMS extends MvcConfiguration {

    public AdminDynamiaCMS() {
        System.err.println("Starting DynamiaCMS application config");
    }

    @Bean
    public LoggingService cmsLoggingService() {
        return new SLF4JLoggingService(AdminDynamiaCMS.class);
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
