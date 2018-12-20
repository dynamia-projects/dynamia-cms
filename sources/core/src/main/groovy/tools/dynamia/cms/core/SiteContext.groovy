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
