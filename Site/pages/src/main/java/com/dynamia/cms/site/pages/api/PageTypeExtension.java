/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.api;

import com.dynamia.cms.site.pages.domain.Page;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public interface PageTypeExtension {

    public String getName();

    public String getViewName();

    public void setupPage(Page page, ModelAndView model);

}
