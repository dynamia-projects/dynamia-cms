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

package tools.dynamia.cms.core.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tools.dynamia.cms.core.domain.City
import tools.dynamia.cms.core.domain.Country
import tools.dynamia.cms.core.domain.Region
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

@RestController
@RequestMapping("/api/locations")
class LocationController {

    @Autowired
    private CrudService service

    @GetMapping("/countries")
    List<Country> getCountries() {
        return service.findAll(Country.class, "name")
    }

    @GetMapping("/countries/{country}/regions")
    List<Region> getRegions(@PathVariable String country) {
        return service.find(Region.class, QueryParameters.with("country.name", country)
                .setAutocreateSearcheableStrings(false).orderBy("name"))

    }

    @GetMapping("/countries/{country}/regions/{regionId}/cities")
    List<City> getCities(@PathVariable String country, @PathVariable Long regionId) {

        return service.find(City.class, QueryParameters.with("region.country.name", country)
                .add("region.id", regionId)
                .setAutocreateSearcheableStrings(false)
                .orderBy("name"))

    }
}
