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
package com.dynamia.cms.site.pages.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "pg_categories")
public class PageCategory extends SimpleEntity implements SiteAware {

    @NotEmpty(message = "Enter category name")
    private String name;
    private String description;
    @OneToOne
    private Site site;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
