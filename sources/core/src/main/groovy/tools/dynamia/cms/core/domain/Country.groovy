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

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_countries")
class Country extends SiteSimpleEntity {

    private String name
    private String code
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Region> regions = new ArrayList<>()
    @JsonIgnore
    private String externalRef


    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    List<Region> getRegions() {
        return regions
    }

    void setRegions(List<Region> regions) {
        this.regions = regions
    }

    @Override
    String toString() {
        return name
    }

    void sync(tools.dynamia.cms.core.dto.CountryDTO dto) {
        name = dto.name
        code = dto.code
        externalRef = dto.externalRef
    }

    String getExternalRef() {
        return externalRef
    }

    void setExternalRef(String externalRef) {
        this.externalRef = externalRef
    }
}
