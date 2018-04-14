package tools.dynamia.cms.site.core.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.dto.CityDTO

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_cities")
public class City extends SiteSimpleEntity {

    @ManyToOne
    @JsonIgnore
    private Region region;
    private String name;
    private String code;
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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, region);
    }

    public void sync(CityDTO dto) {
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
