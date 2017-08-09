package tools.dynamia.cms.site.core.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import tools.dynamia.cms.site.core.dto.RegionDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_regions")
public class Region extends SiteSimpleEntity {

    @ManyToOne
    @JsonIgnore
    private Country country;
    private String name;
    private String code;
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<City> cities = new ArrayList<>();

    private String taxName;
    private double taxPercent;
    @JsonIgnore
    private String externalRef;

    public Region() {
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, country);
    }

    public void sync(RegionDTO dto) {
        name = dto.getName();
        code = dto.getCode();
        taxName = dto.getTaxName();
        taxPercent = dto.getTaxPercent();
        externalRef = dto.getExternalRef();

    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }


}
