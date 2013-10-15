/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import com.dynamia.cms.site.pages.domain.Page;
import java.util.Date;

/**
 *
 * @author mario
 */
public class PageRepository {

    public Page loadPage(String alias) {
        return dummy(alias);
    }

    public Page loadPageByUUID(String uuid) {
        return dummy(uuid);
    }

    private Page dummy(String x) {
        Page p = new Page();
        p.setAlias(x);
        p.setUuid(x);
        p.setTitle("This is the page " + x);
        p.setContent("Welcome to Yarayara <br/> something is <b>happening</b>");
        p.setAuthor("mario");
        p.setPublishDate(new Date());
        p.setCreationDate(new Date());
        return p;


    }
}
