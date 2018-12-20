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
package tools.dynamia.cms.pages.domain

import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "pg_pages_parameters")
class PageParameter extends SimpleEntity {

    @ManyToOne
    @NotNull
    private Page page
    @Column(name = "paramName")
    @NotEmpty(message = "Enter parameter's name")
    private String name
    @Column(name = "paramValue")
    @NotEmpty(message = "Enter parameter's value")
    private String value
    private boolean enabled = true

    Page getPage() {
        return page
    }

    void setPage(Page page) {
        this.page = page
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getValue() {
        return value
    }

    void setValue(String value) {
        this.value = value
    }

    boolean isEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    @Override
    String toString() {
        return value
    }

}
