/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.api;

import com.dynamia.cms.site.pages.PageContext;

/**
 *
 * @author mario
 */
public interface PageTypeExtension {

    public String getId();

    public String getName();

    public String getDescription();

    public String getDescriptorId();

    public void setupPage(PageContext context);

}
