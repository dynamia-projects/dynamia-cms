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
package tools.dynamia.cms.site.templates;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.TemplateEngine;

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
@RequestMapping("/cache")
public class CacheController {

    @Autowired(required = false)
    private CacheManager cacheManager;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    private long lastCacheClear;

    @RequestMapping("/clear/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void clearCache(@PathVariable String name, HttpServletRequest request) {
        if (cacheManager != null) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
        lastCacheClear = System.currentTimeMillis();
    }

    @RequestMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public void clearAllCache() {
        if (cacheManager != null) {
            Collection<String> names = cacheManager.getCacheNames();
            for (String name : names) {
                cacheManager.getCache(name).clear();
            }
        }
        lastCacheClear = System.currentTimeMillis();
    }

    @RequestMapping("/clear/template")
    @ResponseStatus(HttpStatus.OK)
    public void clearTemplate() {
        if (templateEngine != null) {
            templateEngine.clearTemplateCache();
        }
    }

    public long getLastCacheClear() {
        return lastCacheClear;
    }

}
