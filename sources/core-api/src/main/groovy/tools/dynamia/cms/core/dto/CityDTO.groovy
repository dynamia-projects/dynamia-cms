package tools.dynamia.cms.core.dto

class CityDTO extends LocationDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private RegionDTO region

    CityDTO() {
		// TODO Auto-generated constructor stub
	}

    CityDTO(String name, String code, RegionDTO region) {
		super(name, code)
        this.region = region
    }

    RegionDTO getRegion() {
		return region
    }

    void setRegion(RegionDTO region) {
		this.region = region
    }

}
