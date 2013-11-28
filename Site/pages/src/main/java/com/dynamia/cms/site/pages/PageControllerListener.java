/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import com.dynamia.cms.site.pages.domain.Page;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public interface PageControllerListener {

    void onPageLoaded(Page page, ModelAndView mv);

}
