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
package tools.dynamia.cms.core.domain

import tools.dynamia.cms.core.api.Parameter
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_sites_parameters")
class SiteParameter extends SimpleEntity implements Parameter {

	@ManyToOne
	@NotNull
	private Site site
	@NotEmpty(message = "Enter domain Name")
	@NotNull
	private String name
	@Column(name = "paramValue", length=10000)
	private String value
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String extra

	Site getSite() {
		return site
	}

	void setSite(Site site) {
		this.site = site
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

	String getExtra() {
		return extra
	}

	void setExtra(String extra) {
		this.extra = extra
	}

	@Override
	String toString() {
		return name
	}

}
