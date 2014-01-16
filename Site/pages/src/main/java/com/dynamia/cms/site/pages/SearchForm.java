/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import com.dynamia.tools.domain.contraints.NotEmpty;
import java.io.Serializable;

/**
 *
 * @author mario
 */
public class SearchForm implements Serializable{

    @NotEmpty
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
