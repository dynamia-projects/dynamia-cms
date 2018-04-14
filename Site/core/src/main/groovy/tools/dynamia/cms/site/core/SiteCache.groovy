package tools.dynamia.cms.site.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.Cache.ValueWrapper
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.domain.Site
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
        return get(evt.getSite(), key)
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
		Site site = evt.getSite()
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
        keybuilder.append(request.getRequestURI())

        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			keybuilder.append(entry.getKey()).append(Arrays.toString(entry.getValue()))
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

        if (evt.getModelAndView() != null) {
			keybuilder.append(evt.getModelAndView().getViewName())
        }

		if (evt.getRequest() != null) {
			keybuilder.append(evt.getRequest().getRequestURI())
        }

		if (evt.getData() != null) {
			keybuilder.append(evt.getData().toString())
        }

		if (evt.getSource() != null) {
			keybuilder.append(evt.getSource().toString())
        }

		String key = StringUtils.hash(keybuilder.toString(), "MD5")
        return key(evt.getSite(), key)
    }

    String key(Site site, String key) {
		return site.getId() + site.getKey() + ":" + key
    }
}
