package tools.dynamia.cms.core.api

interface DynamiaSiteConnector {

	List<tools.dynamia.cms.core.dto.CountryDTO> getCountries()

    List<tools.dynamia.cms.core.dto.RegionDTO> getRegions(tools.dynamia.cms.core.dto.CountryDTO country)

    List<tools.dynamia.cms.core.dto.CityDTO> getCities(tools.dynamia.cms.core.dto.CountryDTO country, tools.dynamia.cms.core.dto.RegionDTO region)


}
