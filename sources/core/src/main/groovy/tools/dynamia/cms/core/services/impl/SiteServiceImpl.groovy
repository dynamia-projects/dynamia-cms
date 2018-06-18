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
package tools.dynamia.cms.core.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.orm.jpa.EntityManagerFactoryInfo
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.domain.SiteDomain
import tools.dynamia.cms.core.domain.SiteParameter
import tools.dynamia.commons.BeanUtils
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.query.Parameters
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

/**
 * @author Mario Serrano Leones
 */
@Service("siteService")
class SiteServiceImpl implements tools.dynamia.cms.core.services.SiteService {

    private static final String CACHE_NAME = "sites"

    @Autowired
    private EntityManagerFactoryInfo entityManagerFactoryInfo

    @Autowired
    private CrudService crudService

    @Autowired
    private Parameters appParams

    private LoggingService logger = new SLF4JLoggingService(tools.dynamia.cms.core.services.SiteService.class)

    @PostConstruct
    void init() {
        fixOrderableNulls()
        createMainSite()
    }

    @Override
    @Cacheable(value = SiteServiceImpl.CACHE_NAME, key = "#root.methodName")
    Site getMainSite() {
        return crudService.findSingle(Site.class, "key", appParams.getValue(DynamiaCMS.CFG_SUPER_ADMIN_SITE, "main"))
    }

    /**
     * @param key
     * @return
     */
    @Override
    @Cacheable(SiteServiceImpl.CACHE_NAME)
    Site getSite(String key) {
        return crudService.findSingle(Site.class, "key", QueryConditions.eq(key))
    }

    @Override
    @Cacheable(SiteServiceImpl.CACHE_NAME)
    Site getSiteByDomain(String domainName) {
        System.out.println("FINDING SITE FOR DOMAIN: " + domainName)
        Site site = crudService.findSingle(Site.class, "mainDomain", QueryConditions.eq(domainName))
        if (site == null) {
            SiteDomain domain = crudService.findSingle(SiteDomain.class, "name", QueryConditions.eq(domainName))
            site = domain != null ? domain.site : mainSite
        }

        return site
    }

    @Override
    Site getSite(HttpServletRequest request) {
        Site site = null
        if (request != null) {
            tools.dynamia.cms.core.services.SiteService thisServ = Containers.get().findObject(tools.dynamia.cms.core.services.SiteService.class)
            site = thisServ.getSiteByDomain(request.serverName)
        }

        return site
    }

    @Cacheable(value = SiteServiceImpl.CACHE_NAME, key = "'params'+#site.key")
    @Override
    List<SiteParameter> getSiteParameters(Site site) {
        site = crudService.reload(site)
        return site.parameters
    }

    @Override
    SiteParameter getSiteParameter(Site site, String name, String defaultValue) {
        SiteParameter siteParameter = crudService.findSingle(SiteParameter.class,
                QueryParameters.with("site", site).add("name", name))
        if (siteParameter == null) {
            siteParameter = new SiteParameter()
            siteParameter.site = site
            siteParameter.name = name
            siteParameter.value = defaultValue
        }
        return siteParameter
    }

    @Override
    String[] getSiteParameterAsArray(Site site, String name) {
        SiteParameter siteParameter = getSiteParameter(site, name, "")
        String[] values = siteParameter.value.split(",")
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i]
                if (value != null) {
                    values[i] = value.trim()
                }
            }
        } else {
            values = new String[0]
        }

        Arrays.sort(values)

        return values
    }

    @Override
    void clearCache(Site site) {

        List<SiteDomain> domains = crudService.find(SiteDomain.class, "site", site)
        for (SiteDomain domain : domains) {
            if (domain != null) {
                String clearCacheURL = CMSUtil.getSiteURL(domain, "cache/clear")
                String clearTemplateCacheURL = CMSUtil.getSiteURL(domain, "cache/clear/template")

                try {
                    logger.info("Clearing cache for site: " + site + " -> " + clearCacheURL)
                    executeHttpRequest(clearCacheURL)

                    logger.info("Clearing template cache for site: " + site + " -> " + clearTemplateCacheURL)
                    executeHttpRequest(clearTemplateCacheURL)
                } catch (MalformedURLException ex) {
                    logger.error("Invalid site domain URL: " + clearCacheURL + " site:" + site, ex)
                } catch (IOException ex) {
                    logger.error("Error trying to clear site cache. Site: " + site, ex)
                }
            } else {
                logger.warn("Cannot clear site cache " + site + ", not accepted domains configured")
            }
        }
    }

    @Override
    List<Site> getOnlineSites() {
        return crudService.find(Site.class, QueryParameters.with("offline", false))
    }

    private String executeHttpRequest(String url) throws MalformedURLException, IOException {

        StringBuilder sb = new StringBuilder()
        URL request = new URL(url)
        URLConnection yc = request.openConnection()
        BufferedReader reader = new BufferedReader(new InputStreamReader(yc.inputStream))
        String inputLine

        while ((inputLine = reader.readLine()) != null) {
            sb.append(inputLine).append("\n")
        }
        reader.close()

        return sb.toString()
    }

    private void fixOrderableNulls() {
        List<String> entityClasses = entityManagerFactoryInfo.persistenceUnitInfo.managedClassNames
        for (String className : entityClasses) {
            try {
                Object entity = BeanUtils.newInstance(className)
                if (entity instanceof Orderable) {
                    crudService.executeWithinTransaction {
                        crudService.batchUpdate(entity.class, "order", 0,
                                QueryParameters.with("order", QueryConditions.isNull()))
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private void createMainSite() {
        if (crudService.count(Site.class) == 0) {

            Site site = new Site()
            site.key = "main"

            site.description = "Main Site"
            site.name = "boot Main Site"
            site.offline = true
            site.offlineMessage = "Welcome to boot - This site is not configured yet"
            crudService.executeWithinTransaction { crudService.create(site) }
        }
    }

}