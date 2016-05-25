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
package tools.dynamia.cms.site.core;

import tools.dynamia.cms.site.core.domain.ContentAuthor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tools.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.query.Parameters;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
@Component("siteContext")
@Scope("session")
public class SiteContext implements Serializable {

    @Autowired
    private Parameters appParams;

    /**
     *
     */
    private static final long serialVersionUID = 8050753619943744770L;
    private Site current;
    private String siteURL;
    private String currentURI;
    private String currentURL;
    private String previousURI;
    private String previousURL;
    private Map<String, Object> attributes = new HashMap<>();

    public static SiteContext get() {
        return Containers.get().findObject(SiteContext.class);
    }

    public Site getCurrent() {
        return current;
    }

    public void setCurrent(Site current) {
        this.current = current;
    }

    public String getCurrentURI() {
        return currentURI;
    }

    void setCurrentURI(String currentURI) {
        this.previousURI = this.currentURI;
        this.currentURI = currentURI;
    }

    public String getCurrentURL() {
        return currentURL;
    }

    void setCurrentURL(String currentURL) {
        this.previousURL = this.currentURL;
        this.currentURL = currentURL;
    }

    public String getPreviousURI() {
        return previousURI;
    }

    public String getPreviousURL() {
        return previousURL;
    }

    public String getSiteURL() {
        return siteURL;
    }

    void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean isSuperAdmin() {
        String superAdminSite = appParams.getValue(DynamiaCMS.CFG_SUPER_ADMIN_SITE, "main");
        return superAdminSite.equals(SiteContext.get().getCurrent().getKey());
    }

}
