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
package tools.dynamia.cms.site.products.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.actions.ActionEvent;
import tools.dynamia.cms.site.core.actions.SiteAction;
import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.ProductCompareGrid;
import tools.dynamia.cms.site.products.ProductsUtil;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class CompareProducts implements SiteAction {

    @Autowired
    private ProductsService service;
    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return "compareProducts";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String[] idsString = (String[]) evt.getData();
        List<Long> ids = new ArrayList<>();
        for (String id : idsString) {
            ids.add(new Long(id));
        }

        List<Product> products = service.getProductsById(ids);
        ProductCompareGrid grid = new ProductCompareGrid(service, products);
        grid.loadData();

        ModelAndView mv = evt.getModelAndView();
        mv.addObject("comparegrid", grid);
        mv.addObject("title", buildTitle(products, evt.getSite()));
        ProductsUtil.setupDefaultVars(evt.getSite(), mv);
    }

    private String buildTitle(List<Product> products, Site site) {
        CMSUtil utils = new CMSUtil(site);
        List<String> names = new ArrayList<>();
        for (Product product : products) {
            names.add(utils.cropText(product.getName(), 30).replace(".", "").trim().toUpperCase());
        }
        return StringUtils.arrayToDelimitedString(names.toArray(), " vs ");
    }

}
