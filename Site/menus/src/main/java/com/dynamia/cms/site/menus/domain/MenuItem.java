/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus.domain;

import java.io.Serializable;

/**
 *
 * @author mario
 */
public class MenuItem implements Serializable {

    private String name;
    private String page;

    public MenuItem() {
    }

    public MenuItem(String name, String page) {
        this.name = name;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
