/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.contacts.domain

import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.core.domain.SiteSimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "cts_categories")
class ContactCategory extends SiteSimpleEntity implements Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5084812821635888414L
    @NotEmpty(message = "Enter Contact category name")
	private String name
    @Column(length = 1000)
	private String description
    private boolean active = true
    @Column(name="catOrder")
	private int order
    private String defaultEmail

    String getDefaultEmail() {
		return defaultEmail
    }

    void setDefaultEmail(String defaultEmail) {
		this.defaultEmail = defaultEmail
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

    boolean isActive() {
		return active
    }

    void setActive(boolean active) {
		this.active = active
    }

    int getOrder() {
		return order
    }

    void setOrder(int order) {
		this.order = order
    }

	@Override
    String toString() {
		return name
    }

}
