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
