/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.api;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.pages.PageContext;

/**
 *
 * @author mario
 */
@CMSExtension
public class DefaultPageType implements PageTypeExtension {

    @Override
    public String getId() {
        return "default";
    }

    @Override
    public String getName() {
        return "Default Page";
    }

    @Override
    public String getDescription() {
        return "Render the content in plain html";
    }

    @Override
    public String getDescriptorId() {
        return null;
    }

    @Override
    public void setupPage(PageContext context) {
        context.getModelAndView().setViewName("site/page");
    }

}
