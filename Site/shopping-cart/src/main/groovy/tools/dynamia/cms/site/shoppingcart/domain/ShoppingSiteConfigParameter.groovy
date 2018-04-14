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
package tools.dynamia.cms.site.shoppingcart.domain

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
@Table(name = "sc_config_parameters")
class ShoppingSiteConfigParameter extends SimpleEntity {

	@NotNull
	@ManyToOne
	private ShoppingSiteConfig siteConfig
    @NotEmpty(message = "Enter parameter value")
	@NotNull
	private String name
    @Column(name = "paramValue")
	private String value

    ShoppingSiteConfig getSiteConfig() {
		return siteConfig
    }

    void setSiteConfig(ShoppingSiteConfig siteConfig) {
		this.siteConfig = siteConfig
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

	@Override
    String toString() {
		return name
    }

}
