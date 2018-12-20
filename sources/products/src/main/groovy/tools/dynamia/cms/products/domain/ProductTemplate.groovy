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
