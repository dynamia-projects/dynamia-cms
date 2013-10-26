/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.domain;

import com.dynamia.cms.site.core.domain.Content;
import com.dynamia.tools.domain.contraints.NotEmpty;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "pg_pages")
public class Page extends Content {

    @NotEmpty(message = "Enter page title")
    private String title;
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    @OneToOne
    private PageCategory category;
    private String layout;
    

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PageCategory getCategory() {
        return category;
    }

    public void setCategory(PageCategory category) {
        this.category = category;
    }
}
