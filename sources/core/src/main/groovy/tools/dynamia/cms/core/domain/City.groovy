package tools.dynamia.cms.core.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_cities")
class City extends SiteSimpleEntity {

    @ManyToOne
    @JsonIgnore
    private Region region
    private String name
    private String code
    @JsonIgnore
    private String externalRef

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

    Region getRegion() {
        return region
    }

    void setRegion(Region region) {
        this.region = region
    }

    @Override
    String toString() {
        return String.format("%s, %s", name, region)
    }

    void sync(tools.dynamia.cms.core.dto.CityDTO dto) {
        name = dto.name
        code = dto.code
        externalRef = dto.externalRef
    }

    String getExternalRef() {
        return externalRef
    }

    void setExternalRef(String externalRef) {
        this.externalRef = externalRef
    }
}
