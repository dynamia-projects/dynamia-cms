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
package tools.dynamia.cms.site.users.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.City;
import tools.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.util.ContactInfo;

@Entity
@Table(name = "usr_users_contacts", uniqueConstraints = { @UniqueConstraint(columnNames = { "site_id", "user_id" }) })
public class UserContactInfo extends SimpleEntity implements SiteAware {

	@OneToOne
	private Site site;

	@OneToOne
	@NotNull
	@JsonIgnore
	private User user;
	@OneToOne
	private City city;
	private ContactInfo info = new ContactInfo();
	private String name;
	@Column(length = 3000)
	private String description;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ContactInfo getInfo() {
		return info;
	}

	public void setInfo(ContactInfo info) {
		this.info = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("%s (%s, %s)", name, info.getCity(), info.getAddress());
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
