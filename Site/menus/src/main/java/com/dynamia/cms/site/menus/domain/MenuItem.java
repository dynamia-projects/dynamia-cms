/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus.domain;

import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "mn_menuitems")
public class MenuItem extends SimpleEntity implements Serializable {

    @NotEmpty
    private String name;
    @OneToOne
    @NotNull(message = "Select page for menu item")
    private Page page;
    @ManyToOne
    private Menu menu;
    private String icon;
    @Column(name = "itemOrder")
    private int order;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getHref() {
        StringBuilder sb = new StringBuilder();
        if (!menu.getSite().getKey().equals("main")) {
            sb.append("/site/").append(menu.getSite().getKey()).append("/");
        }
        sb.append(getPage().getAlias());
        return sb.toString();
    }

}
