/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author mario
 */
public class SearchForm implements Serializable {

    @NotEmpty
    private String query;

    private HttpServletRequest request;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
