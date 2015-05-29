/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.services.impl;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteDomain;
import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.domain.SiteParameter;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {

	private static final String CACHE_NAME = "sites";

	@Autowired
	private CrudService crudService;

	private LoggingService logger = new SLF4JLoggingService(SiteService.class);

	@Override
	@Cacheable(value = CACHE_NAME, key = "#root.methodName")
	public Site getMainSite() {
		return crudService.findSingle(Site.class, "key", "main");
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	@Override
	@Cacheable(CACHE_NAME)
	public Site getSite(String key) {
		return crudService.findSingle(Site.class, "key", key);
	}

	@Override
	@Cacheable(CACHE_NAME)
	public Site getSiteByDomain(String domainName) {
		System.out.println("FINDING SITE FOR DOMAIN: " + domainName);
		SiteDomain domain = crudService.findSingle(SiteDomain.class, "name", domainName);

		return domain != null ? domain.getSite() : null;
	}

	@Override
	public Site getSite(HttpServletRequest request) {
		Site site = null;
		if (request != null) {
			SiteService thisServ = Containers.get().findObject(SiteService.class);
			site = thisServ.getSiteByDomain(request.getServerName());
		}
		return site;
	}

	@Cacheable(value = CACHE_NAME, key = "'params'+#site.key")
	@Override
	public List<SiteParameter> getSiteParameters(Site site) {
		site = crudService.reload(site);
		return site.getParameters();
	}

	@Override
	public SiteParameter getSiteParameter(Site site, String name, String defaultValue) {
		SiteParameter siteParameter = crudService.findSingle(SiteParameter.class, QueryParameters.with("site", site).add("name", name));
		if (siteParameter == null) {
			siteParameter = new SiteParameter();
			siteParameter.setSite(site);
			siteParameter.setName(name);
			siteParameter.setValue(defaultValue);
		}
		return siteParameter;
	}

	@Override
	public String[] getSiteParameterAsArray(Site site, String name) {
		SiteParameter siteParameter = getSiteParameter(site, name, "");
		String[] values = siteParameter.getValue().split(",");
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				if (value != null) {
					values[i] = value.trim();
				}
			}
		} else {
			values = new String[0];
		}

		Arrays.sort(values);

		return values;
	}

	@Override
	public void clearCache(Site site) {
		site = crudService.reload(site);
		List<SiteDomain> domains = site.getAcceptedDomains();
		for (SiteDomain domain : domains) {
			if (domain != null) {
				String urltext = String.format("http://%s/cache/clear", domain.getName());
				if (domain.getPort() > 0) {
					urltext = String.format("http://%s:%s/cache/clear", domain.getName(), domain.getPort());
				}
				try {
					logger.info("Clearing cache for site: " + site + " -> " + urltext);
					executeHttpRequest(urltext);
				} catch (MalformedURLException ex) {
					logger.error("Invalid site domain URL: " + urltext + " site:" + site, ex);
				} catch (IOException ex) {
					logger.error("Error trying to clear site cache. Site: " + site, ex);
				}
			} else {
				logger.warn("Cannot clear site cache " + site + ", not accepted domains configured");
			}
		}
	}

	private String executeHttpRequest(String url) throws MalformedURLException, IOException {

		StringBuilder sb = new StringBuilder();
		URL request = new URL(url);
		URLConnection yc = request.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine).append("\n");
		}
		in.close();

		return sb.toString();
	}

}
