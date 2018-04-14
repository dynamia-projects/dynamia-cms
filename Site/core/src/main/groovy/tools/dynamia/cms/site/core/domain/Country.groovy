package tools.dynamia.cms.site.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tools.dynamia.cms.site.core.dto.CountryDTO;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_countries")
public class Country extends SiteSimpleEntity {

    private String name;
    private String code;
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Region> regions = new ArrayList<>();
    @JsonIgnore
    private String externalRef;


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

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @Override
    public String toString() {
        return name;
    }

    public void sync(CountryDTO dto) {
        name = dto.getName();
        code = dto.getCode();
        externalRef = dto.getExternalRef();
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }
}
