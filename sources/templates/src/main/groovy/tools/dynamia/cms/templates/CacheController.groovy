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
package tools.dynamia.cms.templates

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.thymeleaf.TemplateEngine

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
@RequestMapping("/cache")
class CacheController {

    @Autowired(required = false)
    private CacheManager cacheManager

    @Autowired(required = false)
    private TemplateEngine templateEngine

    private long lastCacheClear

    @RequestMapping("/clear/{name}")
    @ResponseStatus(HttpStatus.OK)
    void clearCache(@PathVariable String name, HttpServletRequest request) {
        if (cacheManager != null) {
            Cache cache = cacheManager.getCache(name)
            if (cache != null) {
                cache.clear()
            }
        }
        lastCacheClear = System.currentTimeMillis()
    }

    @RequestMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    void clearAllCache() {
        if (cacheManager != null) {
            Collection<String> names = cacheManager.cacheNames
            for (String name : names) {
                cacheManager.getCache(name).clear()
            }
        }
        lastCacheClear = System.currentTimeMillis()
        clearTemplate()
    }

    @RequestMapping("/clear/template")
    @ResponseStatus(HttpStatus.OK)
    void clearTemplate() {
        templateEngine?.clearTemplateCache()
    }

    long getLastCacheClear() {
        return lastCacheClear
    }

}
