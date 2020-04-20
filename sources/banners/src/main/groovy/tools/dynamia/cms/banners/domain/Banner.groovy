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
package tools.dynamia.cms.banners.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Lob
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "ban_banners")
@BatchSize(size = 10)
class Banner extends SimpleEntity implements SiteAware, Orderable {

    /**
     *
     */
    static final long serialVersionUID = 2090565396360605396L

    @OneToOne
    @NotNull
    Site site

    String title
    String subtitle
    String description
    boolean enabled = true
    String url = "#"
    String imageURL
    String alternateImageURL
    @Column(name = "bannerOrder")
    int order
    String buttonLabel
    String buttonStyleClass
    boolean titleVisible = true
    String styleClass
    @Lob
    @Basic(fetch = FetchType.LAZY)
    String htmlContent
    boolean  useHtmlContent

    @OneToOne
    @NotNull
    BannerCategory category

    @Override
    String toString() {
        return title
    }


}
