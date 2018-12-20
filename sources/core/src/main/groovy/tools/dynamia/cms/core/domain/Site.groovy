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

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tools.dynamia.cms.core.api.Parameterizable
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_sites")
class Site extends SimpleEntity implements Parameterizable {

    @NotEmpty
    String name
    @Column(name = "site_key", unique = true)
    @NotEmpty
    String key
    @OneToMany(mappedBy = "site")
    @JsonIgnore
    List<SiteDomain> acceptedDomains = new ArrayList<>()

    @OneToMany(mappedBy = "site")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<SiteParameter> parameters = new ArrayList<>()

    @OneToOne
    @JsonIgnore
    Site parent

    String description
    boolean offline
    String offlineMessage
    String offlineIcon
    String offlineRedirect
    String template = "dynamical"
    String metadataKeywords
    String metadataAuthor
    String metadataDescription
    String metadataRights
    String googleAnalyticsID
    String googleAnalyticsDomain
    String googleSiteVerification
    boolean corporateSite
    String mainDomain

    String logoURL
    String twitter
    String googlePlus
    String facebook
    String instagram
    String linkedin
    String tumblr
    String snapchat
    String pinterest
    String vk
    String flickr
    String vine
    String meetup
    boolean searchEnabled

    String externalConnectorURL

 

    String getParameterValue(String name) {
        for (SiteParameter siteParameter : parameters) {
            if (siteParameter.name.equals(name)) {
                return siteParameter.value
            }
        }
        return null
    }

    boolean isParameter(String name) {
        String value = getParameterValue(name)
        return value == null ? false : Boolean.valueOf(value)
    }

    @Override
    String toString() {
        return String.format("%s (%s)", name, key)
    }

}
