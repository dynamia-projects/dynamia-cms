/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.controllers;

import java.util.Collection;
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
 * @author mario
 */
@Controller
@RequestMapping("/cache")
public class CacheController {

    @Autowired(required = false)
    private CacheManager cacheManager;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @RequestMapping("/clear/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void clearCache(@PathVariable String name) {
        if (cacheManager != null) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
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
    }

    @RequestMapping("/clear/template")
    @ResponseStatus(HttpStatus.OK)
    public void clearTemplate() {
        if (templateEngine != null) {
            templateEngine.clearTemplateCache();
        }
    }
}
