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
package tools.dynamia.cms.core.domain

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.domain.BaseEntity

import javax.persistence.MappedSuperclass
import javax.persistence.OneToOne
import javax.validation.constraints.NotNull

@MappedSuperclass
abstract class SiteBaseEntity extends BaseEntity implements SiteAware {

	/**
	 *
	 */
	private static final long serialVersionUID = -8508258881044370132L
    @OneToOne
	@NotNull
	private Site site

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

}
