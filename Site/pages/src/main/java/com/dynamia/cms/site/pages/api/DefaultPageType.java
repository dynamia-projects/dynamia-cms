/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.api;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.pages.domain.Page;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSExtension
public class DefaultPageType implements PageTypeExtension {

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public String getViewName() {
        return "site/page";
    }

    @Override
    public void setupPage(Page page, ModelAndView model) {

    }

}
