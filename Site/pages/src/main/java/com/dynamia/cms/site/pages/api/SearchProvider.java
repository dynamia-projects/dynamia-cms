/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.api;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.SearchForm;

/**
 *
 * @author mario
 */
public interface SearchProvider {

    public SearchResult search(Site site, SearchForm form);

}
