package tools.dynamia.cms.site.core.api

import tools.dynamia.cms.site.core.dto.CityDTO
import tools.dynamia.cms.site.core.dto.CountryDTO
import tools.dynamia.cms.site.core.dto.RegionDTO

interface DynamiaSiteConnector {

	List<CountryDTO> getCountries()

    List<RegionDTO> getRegions(CountryDTO country)

    List<CityDTO> getCities(CountryDTO country, RegionDTO region)


}
