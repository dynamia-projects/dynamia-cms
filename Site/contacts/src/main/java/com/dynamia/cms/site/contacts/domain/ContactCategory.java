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
package com.dynamia.cms.site.contacts.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.core.domain.SiteSimpleEntity;

import tools.dynamia.domain.contraints.NotEmpty;

@Entity
@Table(name = "cts_categories")
public class ContactCategory extends SiteSimpleEntity implements Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5084812821635888414L;
	@NotEmpty(message = "Enter Contact category name")
	private String name;
	@Column(length = 1000)
	private String description;
	private boolean active = true;
	@Column(name="catOrder")
	private int order;
	private String defaultEmail;

	public String getDefaultEmail() {
		return defaultEmail;
	}

	public void setDefaultEmail(String defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return name;
	}

}
