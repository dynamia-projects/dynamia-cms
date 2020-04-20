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
