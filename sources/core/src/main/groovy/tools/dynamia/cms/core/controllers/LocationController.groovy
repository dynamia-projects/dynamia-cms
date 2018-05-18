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
