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
