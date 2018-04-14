package tools.dynamia.cms.site.core.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.dto.CountryDTO

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_countries")
class Country extends SiteSimpleEntity {

    private String name
    private String code
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Region> regions = new ArrayList<>()
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

    List<Region> getRegions() {
        return regions
    }

    void setRegions(List<Region> regions) {
        this.regions = regions
    }

    @Override
    String toString() {
        return name
    }

    void sync(CountryDTO dto) {
        name = dto.getName()
        code = dto.getCode()
        externalRef = dto.getExternalRef()
    }

    String getExternalRef() {
        return externalRef
    }

    void setExternalRef(String externalRef) {
        this.externalRef = externalRef
    }
}
