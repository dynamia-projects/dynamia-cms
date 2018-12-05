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
