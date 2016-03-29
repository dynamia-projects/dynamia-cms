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
package com.dynamia.cms.admin.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class PageAutoPublisher {

    @Autowired
    private PageService service;

    private LoggingService logger = new SLF4JLoggingService(PageAutoPublisher.class);

    @Scheduled(fixedDelay = 10 * 60 * 60 * 1000)
    public void sync() {
        try {
            logger.info("Publishing pages for today");
            int r = service.publishPages();
            logger.info(r + " pages published ");
        } catch (Exception e) {
            logger.error("Error publishng pages", e);
        }

        try {
            logger.info("Unpublishing old pages");
            int r = service.unpublishPages();
            logger.info(r + " pages unpublished ");
        } catch (Exception e) {
            logger.error("Error unpublishng pages", e);
        }
    }

}
