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
