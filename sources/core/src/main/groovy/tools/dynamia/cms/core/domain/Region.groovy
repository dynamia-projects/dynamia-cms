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

import javax.persistence.*

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_regions")
class Region extends SiteSimpleEntity {

    @ManyToOne
    @JsonIgnore
    private Country country
    private String name
    private String code
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<City> cities = new ArrayList<>()

    private String taxName
    private double taxPercent
    @JsonIgnore
    private String externalRef

    Region() {
    }

    Country getCountry() {
        return country
    }

    void setCountry(Country country) {
        this.country = country
    }

    String getTaxName() {
        return taxName
    }

    void setTaxName(String taxName) {
        this.taxName = taxName
    }

    double getTaxPercent() {
        return taxPercent
    }

    void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent
    }

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

    List<City> getCities() {
        return cities
    }

    void setCities(List<City> cities) {
        this.cities = cities
    }

    @Override
    String toString() {
        return String.format("%s, %s", name, country)
    }

    void sync(tools.dynamia.cms.core.dto.RegionDTO dto) {
        name = dto.name
        code = dto.code
        taxName = dto.taxName
        taxPercent = dto.taxPercent
        externalRef = dto.externalRef

    }

    String getExternalRef() {
        return externalRef
    }

    void setExternalRef(String externalRef) {
        this.externalRef = externalRef
    }


}
