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
package tools.dynamia.cms.products.domain

import tools.dynamia.cms.core.domain.SiteBaseEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_templates")
class ProductTemplate extends SiteBaseEntity {

    @NotEmpty(message = "Enter template name")
    private String name
    @Column(length = 2000)
    private String description
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content
    private boolean enabled
    private String templateEngine

    String getTemplateEngine() {
        return templateEngine
    }

    void setTemplateEngine(String templateEngine) {
        this.templateEngine = templateEngine
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    String getContent() {
        return content
    }

    void setContent(String content) {
    	
        this.content = content
    }

    boolean isEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    @Override
    String toString() {
        return name
    }

}
