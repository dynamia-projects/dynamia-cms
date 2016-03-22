/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.products.actions;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.services.ProductsService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowBrandsAction implements SiteAction {

    @Autowired
    private ProductsService service;

    @Override
    public String getName() {
        return "showProductBrands";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        mv.addObject("title", "Nuestras marcas");
        mv.addObject("brandlist", service.getBrands(evt.getSite()));

        ProductsUtil.setupProductsVar(Collections.EMPTY_LIST, mv);
    }

}
