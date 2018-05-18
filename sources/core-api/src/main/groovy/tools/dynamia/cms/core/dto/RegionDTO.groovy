package tools.dynamia.cms.core.dto

class RegionDTO extends LocationDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L

    private CountryDTO country
    private String taxName
    private double taxPercent

    RegionDTO() {

	}

    RegionDTO(String name, String code, CountryDTO country) {
		super(name, code)
        this.country = country
    }

    CountryDTO getCountry() {
		return country
    }

    void setCountry(CountryDTO country) {
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

}
