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

package tools.dynamia.cms.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.Cache.ValueWrapper
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.commons.StringUtils

import javax.servlet.http.HttpServletRequest
import java.util.Map.Entry

@Component
class SiteCache {

	@Autowired
	private CacheManager cacheManager

    public static final String CACHE_NAME = "sitesCache"

    Object get(Site site, String key) {
		Cache cache = cacheManager.getCache(CACHE_NAME)
        ValueWrapper wrapper = cache.get(key(site, key))
        if (wrapper != null) {
			return wrapper.get()
        }

		return null
    }

    Object get(ActionEvent evt, String keysalt) {
		String key = key(evt, keysalt)
        return get(evt.site, key)
    }

    Object get(Site site, HttpServletRequest request, String keysalt) {
		String key = key(site,request, keysalt)
        return get(site, key)
    }

    void put(Site site, String key, Object object) {
		Cache cache = cacheManager.getCache(CACHE_NAME)
        cache.put(key(site, key), object)
    }

	/**
	 * Put object to site cache and generate a unique key for ActionEvent using
	 * its properties.
	 * 
	 * @param evt
	 * @param keysalt
	 * @param object
	 */
	void put(ActionEvent evt, String keysalt, Object object) {
		Site site = evt.site
        String key = key(evt, keysalt)
        put(site, key, object)
    }

	/**
	 * Put to site cache object and generate a unique key for request using
	 * request URI, and parameters map. Keysalt is for customize the generated
	 * key.
	 * 
	 * @param site
	 * @param request
	 * @param keysalt
	 * @param object
	 */
	void put(Site site, HttpServletRequest request, String keysalt, Object object) {
		String key = key(site, request, keysalt)
        put(site, key, object)
    }

	/**
	 * Generate an unique key for site using request URI and parameters map
	 * 
	 * @param site
	 * @param request
	 * @param keysalt
	 * @return
	 */
	String key(Site site, HttpServletRequest request, String keysalt) {
		StringBuilder keybuilder = new StringBuilder()

        keybuilder.append(keysalt)
        keybuilder.append(request.requestURI)

        for (Entry<String, String[]> entry : request.parameterMap.entrySet()) {
			keybuilder.append(entry.key).append(Arrays.toString(entry.value))
        }

		String key = StringUtils.hash(keybuilder.toString(), "MD5")
        return key(site, key)
    }

	/**
	 * Generate an unique key for site using ActionEvent properties:
	 * ModelAndView.viewName, Request.requestURI and data.toString().
	 * 
	 * @param evt
	 * @param keysalt
	 * @return
	 */
	String key(ActionEvent evt, String keysalt) {
		StringBuilder keybuilder = new StringBuilder()

        keybuilder.append(keysalt)

        if (evt.modelAndView != null) {
			keybuilder.append(evt.modelAndView.viewName)
        }

		if (evt.request != null) {
			keybuilder.append(evt.request.requestURI)
        }

		if (evt.data != null) {
			keybuilder.append(evt.data.toString())
        }

		if (evt.source != null) {
			keybuilder.append(evt.source.toString())
        }

		String key = StringUtils.hash(keybuilder.toString(), "MD5")
        return key(evt.site, key)
    }

    String key(Site site, String key) {
		return site.id + site.key + ":" + key
    }
}
