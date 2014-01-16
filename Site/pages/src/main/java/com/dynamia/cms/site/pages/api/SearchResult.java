/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mario
 */
public class SearchResult implements Serializable {

    private String viewName = "site/page";
    private final Map<String, Object> entries = new HashMap<>();
    private boolean keepSearching;

    public SearchResult() {
    }

    public SearchResult(String viewName, boolean keepSearching) {
        this.viewName = viewName;
        this.keepSearching = keepSearching;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public boolean isKeepSearching() {
        return keepSearching;
    }

    public void setKeepSearching(boolean keepSearching) {
        this.keepSearching = keepSearching;

    }

    public void addEntry(String name, Object value) {
        entries.put(name, value);
    }

    public Map<String, Object> getEntries() {
        return entries;
    }

}
