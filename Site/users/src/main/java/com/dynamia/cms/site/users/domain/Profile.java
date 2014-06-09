/**
 * Dynamia Soluciones IT Todos los Derechos Reservados (c) Colombia
 *
 * Prohibida la reproducción parcial o total de este archivo de código fuente en
 * proyectos de software NO realizados por Dynamia Soluciones IT. Cualquier otro
 * archivo de código fuente o librería que haga referencia a este tendrá la
 * misma licencia.
 *
 * mas info: http://www.dynamiasoluciones.com/licencia.html
 */
package com.dynamia.cms.site.users.domain;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import com.dynamia.tools.domain.ValidationError;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

/**
 * Archivo: Perfil.java Fecha de creación: 27/06/2009
 *
 * @author Ing. Mario Serrano Leones
 */
@Entity
@Table(name = "usr_profiles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"site_id", "internalName"})
})
public class Profile extends BaseEntity implements GrantedAuthority, SiteAware {

    @OneToOne
    @NotNull
    private Site site;

    @NotNull(message = "Ingrese nombre del perfil")
    private String name;
    private String description;
    private String internalName;
    private boolean editable = true;
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<UserGrantedAuthority> grantedAuthorities = new ArrayList<>();
    @OneToMany(mappedBy = "profile")
    private List<UserProfile> users = new ArrayList<>();

    @Override
    public Site getSite() {
        return site;
    }

    /**
     *
     * @param site
     */
    public void setSite(Site site) {
        this.site = site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        if (internalName != null) {
            this.internalName = "ROLE_" + internalName.toUpperCase().replace(" ", "_");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setInternalName(name);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public List<UserGrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<UserGrantedAuthority> ga) {
        this.grantedAuthorities = ga;
    }

    public List<UserProfile> getUsers() {
        return users;
    }

    public void setUsers(List<UserProfile> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return internalName;
    }

    public int compareTo(Object o) {
        if (o != null && o instanceof GrantedAuthority) {
            String rhsRole = ((GrantedAuthority) o).getAuthority();

            if (rhsRole == null) {
                return -1;
            }

            return getAuthority().compareTo(rhsRole);
        }
        return -1;
    }

    public void addGrantedAuthority(UserGrantedAuthority p) {
        if (p != null) {
            for (UserGrantedAuthority ga : grantedAuthorities) {
                if (ga.getType().equals(p.getType()) && ga.getValue().equals(p.getValue())) {
                    throw new ValidationError("Selected granted authority already assigned");
                }
            }

            grantedAuthorities.add(p);
            p.setProfile(this);
        }
    }

    public void removeGrantedAuthority(UserGrantedAuthority p) {
        if (p != null) {
            grantedAuthorities.remove(p);
            p.setProfile(null);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
