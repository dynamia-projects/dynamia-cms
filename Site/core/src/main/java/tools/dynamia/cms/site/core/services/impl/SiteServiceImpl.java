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
package tools.dynamia.cms.site.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.Orderable;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.domain.SiteDomain;
import tools.dynamia.cms.site.core.domain.SiteParameter;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.commons.BeanUtils;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.Parameters;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.scheduling.Task;

/**
 *
 * @author Mario Serrano Leones
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {

	private static final String CACHE_NAME = "sites";

	@Autowired
	private EntityManagerFactoryInfo entityManagerFactoryInfo;

	@Autowired
	private CrudService crudService;

	@Autowired
	private Parameters appParams;

	private LoggingService logger = new SLF4JLoggingService(SiteService.class);

	@PostConstruct
	public void init() {
		fixOrderableNulls();
		createMainSite();
	}

	@Override
	@Cacheable(value = CACHE_NAME, key = "#root.methodName")
	public Site getMainSite() {
		return crudService.findSingle(Site.class, "key", appParams.getValue(DynamiaCMS.CFG_SUPER_ADMIN_SITE, "main"));
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

		return domain != null ? domain.getSite() : getMainSite();
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
		SiteParameter siteParameter = crudService.findSingle(SiteParameter.class,
				QueryParameters.with("site", site).add("name", name));
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

		List<SiteDomain> domains = crudService.find(SiteDomain.class, "site", site);
		for (SiteDomain domain : domains) {
			if (domain != null) {
				String clearCacheURL = CMSUtil.getSiteURL(domain, "cache/clear");
				String clearTemplateCacheURL = CMSUtil.getSiteURL(domain, "cache/clear/template");

				try {
					logger.info("Clearing cache for site: " + site + " -> " + clearCacheURL);
					executeHttpRequest(clearCacheURL);

					logger.info("Clearing template cache for site: " + site + " -> " + clearTemplateCacheURL);
					executeHttpRequest(clearTemplateCacheURL);
				} catch (MalformedURLException ex) {
					logger.error("Invalid site domain URL: " + clearCacheURL + " site:" + site, ex);
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

	private void fixOrderableNulls() {
		List<String> entityClasses = entityManagerFactoryInfo.getPersistenceUnitInfo().getManagedClassNames();
		for (String className : entityClasses) {
			try {
				Object entity = BeanUtils.newInstance(className);
				if (entity instanceof Orderable) {
					crudService.executeWithinTransaction(new Task() {
						@Override
						public void doWork() {
							crudService.batchUpdate(entity.getClass(), "order", 0,
									QueryParameters.with("order", QueryConditions.isNull()));
						}
					});
				}
			} catch (Exception e) {

			}
		}
	}

	private void createMainSite() {
		if (crudService.count(Site.class) == 0) {

			Site site = new Site();
			site.setKey("main");

			site.setDescription("Main Site");
			site.setName("DynamiaCMS Main Site");
			site.setOffline(true);
			site.setOfflineMessage("Welcome to DynamiaCMS - This site is not configured yet");
			crudService.executeWithinTransaction(() -> crudService.create(site));
		}
	}

}
