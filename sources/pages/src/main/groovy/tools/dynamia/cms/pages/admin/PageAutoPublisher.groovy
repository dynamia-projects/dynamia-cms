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
package tools.dynamia.cms.pages.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.dynamia.cms.pages.services.PageService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class PageAutoPublisher {

    @Autowired
    private PageService service

    private LoggingService logger = new SLF4JLoggingService(PageAutoPublisher.class)

    @Scheduled(fixedDelay = 36000000L)
    void sync() {
        try {
            logger.info("Publishing pages for now")
            int r = service.publishPages()
            logger.info(r + " pages published ")
        } catch (Exception e) {
            logger.error("Error publishng pages", e)
        }

        try {
            logger.info("Unpublishing old pages")
            int r = service.unpublishPages()
            logger.info(r + " pages unpublished ")
        } catch (Exception e) {
            logger.error("Error unpublishng pages", e)
        }
    }

}
