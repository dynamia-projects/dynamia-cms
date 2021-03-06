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

package tools.dynamia.cms.core.services.impl

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.dto.CountryDTO
import tools.dynamia.cms.core.dto.RegionDTO
import tools.dynamia.cms.core.services.DynamiaSiteConnectorService
import tools.dynamia.cms.core.api.DynamiaSiteConnector
import tools.dynamia.cms.core.domain.City
import tools.dynamia.cms.core.domain.Country
import tools.dynamia.cms.core.domain.Region
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.dto.CityDTO
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.sterotypes.Service
import tools.dynamia.web.util.HttpRemotingServiceClient

@Service
class DynamiaSiteConnectorServiceImpl implements DynamiaSiteConnectorService {

    @Autowired
    private CrudService crudService


    @Override
    void sync(Site site) {

        if (site != null && site.externalConnectorURL != null && !site.externalConnectorURL.empty) {

            DynamiaSiteConnector connector = HttpRemotingServiceClient.build(DynamiaSiteConnector.class)
                    .setServiceURL(site.externalConnectorURL)
                    .getProxy()

            if (connector != null) {
                crudService.executeWithinTransaction {
                    syncLocations(site, connector)
                }
            } else {
                throw new ValidationError("Cannot connect to external site sync service: " + site.externalConnectorURL)
            }

        } else {
            throw new ValidationError("No site sync configuration found")
        }

    }


    private void syncLocations(Site site, DynamiaSiteConnector connector) {

        List<CountryDTO> countries = connector.countries
        if (countries != null) {

            countries.forEach { countryDTO ->
                Country country = getOrCreate(site, countryDTO)
                System.out.println("Loading Country: " + country)
                List<RegionDTO> regions = connector.getRegions(countryDTO)
                regions.forEach { regionDTO ->

                    Region region = getOrCreate(site, regionDTO, country)
                    System.out.println("---: " + region)
                    List<CityDTO> cities = connector.getCities(countryDTO, regionDTO)
                    cities.forEach { cityDTO ->
                        System.out.println("---------: " + cityDTO.name)
                        createOrUpdateCity(site, cityDTO, region)
                    }
                }
            }

        }

    }

    private void createOrUpdateCity(Site site, CityDTO dto, Region region) {
        City city = crudService.findSingle(City.class, QueryParameters.with("site", site)
                .add("region", region)
                .add("externalRef", dto.externalRef))

        if (city == null) {
            city = new City()
            city.site = site
            city.region = region
        }

        city.sync(dto)

        crudService.save(city)
    }

    private Region getOrCreate(Site site, tools.dynamia.cms.core.dto.RegionDTO dto, Country country) {

        Region region = crudService.findSingle(Region.class, QueryParameters.with("site", site)
                .add("country", country)
                .add("externalRef", QueryConditions.eq(dto.externalRef)))

        if (region == null) {
            region = new Region()
            region.country = country
            region.site = site
        }

        region.sync(dto)

        region = crudService.save(region)


        return region
    }

    private Country getOrCreate(Site site, tools.dynamia.cms.core.dto.CountryDTO dto) {


        Country country = crudService.findSingle(Country.class, QueryParameters.with("site", site)
                .add("externalRef", QueryConditions.eq(dto.externalRef)))

        if (country == null) {
            country = new Country()
            country.site = site
        }

        country.sync(dto)
        country = crudService.save(country)

        return country
    }
}
