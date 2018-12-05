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
