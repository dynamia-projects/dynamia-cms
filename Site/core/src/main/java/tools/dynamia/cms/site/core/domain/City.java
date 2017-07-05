package tools.dynamia.cms.site.core.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by mario on 4/07/2017.
 */
@Entity
@Table(name = "cr_loc_cities")
public class City extends SiteSimpleEntity {

    @ManyToOne
    private Region region;
    private String name;
    private String code;

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
}
