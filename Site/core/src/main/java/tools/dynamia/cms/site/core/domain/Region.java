package tools.dynamia.cms.site.core.domain;


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
    private Country country;
    private String name;
    private String code;
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<City> cities = new ArrayList<>();

    private String taxName;
    private double taxPercent;

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
        return String.format("%s, %s",name,country);
    }
}
