/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.query.Parameters
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@Component("siteContext")
@Scope("session")
class SiteContext implements Serializable {

	@Autowired
	private Parameters appParams

    @Autowired
	private tools.dynamia.cms.core.services.SiteService service

    /**
	 *
	 */
	private static final long serialVersionUID = 8050753619943744770L
    private tools.dynamia.cms.core.domain.Site current
    private String siteURL
    private String currentURI
    private String currentURL
    private String previousURI
    private String previousURL
    private Map<String, Object> attributes = new HashMap<>()

    static SiteContext get() {
		return Containers.get().findObject(SiteContext.class)
    }

    tools.dynamia.cms.core.domain.Site getCurrent() {
		return current
    }

    void setCurrent(tools.dynamia.cms.core.domain.Site current) {
		this.current = current
    }

    String getCurrentURI() {
		return currentURI
    }

	void setCurrentURI(String currentURI) {
		this.previousURI = this.currentURI
        this.currentURI = currentURI
    }

    String getCurrentURL() {
		return currentURL
    }

	void setCurrentURL(String currentURL) {
		this.previousURL = this.currentURL
        this.currentURL = currentURL
    }

    String getPreviousURI() {
		return previousURI
    }

    String getPreviousURL() {
		return previousURL
    }

    String getSiteURL() {
		return siteURL
    }

	void setSiteURL(String siteURL) {
		this.siteURL = siteURL
    }

    void setAttribute(String name, Object value) {
		attributes.put(name, value)
    }

    Object getAttribute(String name) {
		return attributes.get(name)
    }

    boolean isSuperAdmin() {
		String superAdminSite = appParams.getValue(DynamiaCMS.CFG_SUPER_ADMIN_SITE, "main")
        return superAdminSite.equals(SiteContext.get().current.key)
    }

    void reload() {
		if (current != null) {
			Site reloaded = service.getSite(current.key)
            if (reloaded != null) {
				current = reloaded
            }
		}

	}

}
