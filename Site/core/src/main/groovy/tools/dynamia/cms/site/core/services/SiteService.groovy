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
package tools.dynamia.cms.site.core.services

import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.domain.SiteParameter

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
public interface SiteService {

	Site getMainSite();

	Site getSite(String key);

	Site getSiteByDomain(String domainName);

	Site getSite(HttpServletRequest request);

	List<SiteParameter> getSiteParameters(Site site);

	SiteParameter getSiteParameter(Site site, String name, String defaultValue);

	public void clearCache(Site site);

	public abstract String[] getSiteParameterAsArray(Site site, String name);

	public List<Site> getOnlineSites();

}
