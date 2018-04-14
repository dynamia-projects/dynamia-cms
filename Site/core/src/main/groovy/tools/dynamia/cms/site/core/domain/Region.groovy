package tools.dynamia.cms.site.core.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.dto.RegionDTO

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

    void sync(RegionDTO dto) {
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
