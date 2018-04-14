package tools.dynamia.cms.site.core.api

import tools.dynamia.cms.site.core.dto.CityDTO
import tools.dynamia.cms.site.core.dto.CountryDTO
import tools.dynamia.cms.site.core.dto.RegionDTO

public interface DynamiaSiteConnector {

	public List<CountryDTO> getCountries();

	public List<RegionDTO> getRegions(CountryDTO country);

	public List<CityDTO> getCities(CountryDTO country, RegionDTO region);
	
	

}
