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
package com.dynamia.cms.admin.core;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
@EnableScheduling
@EnableAsync
public class AppConfig {

    @Bean
    public LoggingService cmsLoggingService() {
        return new SLF4JLoggingService(AppConfig.class);
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
