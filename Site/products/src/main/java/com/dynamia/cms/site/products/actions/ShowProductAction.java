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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.pages.PageNotFoundException;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductStock;
import com.dynamia.cms.site.products.domain.ProductUserStory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductTemplateService;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.users.UserHolder;
import java.util.Collection;
import java.util.HashMap;
import tools.dynamia.commons.CollectionsUtils;
import tools.dynamia.commons.collect.CollectionWrapper;

import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowProductAction implements SiteAction {

    @Autowired
    private ProductsService service;

    @Autowired
    private ProductTemplateService templateService;

    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return "showProduct";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();

        Product product = null;
        Long id = (Long) evt.getData();
        QueryParameters qp = QueryParameters.with("active", true).add("site", evt.getSite());

        if (id != null) {
            qp.add("id", id);
        } else if (evt.getRequest().getParameter("sku") != null) {
            String sku = evt.getRequest().getParameter("sku");
            qp.add("sku", sku);

        }
        product = crudService.findSingle(Product.class, qp);

        if (product == null) {
            throw new PageNotFoundException("Product not found");
        }

        String price = "";
        try {
            CMSUtil util = new CMSUtil(evt.getSite());
            ProductsSiteConfig config = service.getSiteConfig(evt.getSite());
            price = " - " + util.formatNumber(product.getPrice(), config.getPricePattern());
        } catch (Exception e) {
        }

        service.updateViewsCount(product);
        service.updateProductStoryViews(product);
        ProductUserStory story = service.getProductStory(product, UserHolder.get().getCurrent());
        if (story != null) {
            mv.addObject("prd_story", story);
        }

        loadStockDetails(product, mv);
        
        mv.addObject("prd_product", product);        
        mv.addObject("prd_relatedProducts", service.getRelatedProducts(product));
        mv.addObject("prd_config", service.getSiteConfig(evt.getSite()));
        mv.addObject("title", product.getName().toUpperCase() + price);
        mv.addObject("subtitle", product.getCategory().getName());
        mv.addObject("icon", "info-sign");

        mv.addObject("metaDescription", product.getDescription());
        if (product.getTags() != null && !product.getTags().isEmpty()) {
            mv.addObject("metaKeywords", product.getTags());
        }

        String baseImageUrl = SiteContext.get().getSiteURL() + "/resources/products/images/";
        List<String> pageImages = new ArrayList<>();
        pageImages.add(baseImageUrl + product.getImage());
        if (product.getImage2() != null) {
            pageImages.add(baseImageUrl + product.getImage2());
        }
        if (product.getImage3() != null) {
            pageImages.add(baseImageUrl + product.getImage3());
        }
        if (product.getImage4() != null) {
            pageImages.add(baseImageUrl + product.getImage4());
        }
        mv.addObject("pageImages", pageImages);
        mv.addObject("baseImageUrl", baseImageUrl);

        if (templateService.hasTemplate(product)) {
            mv.addObject("prd_hasTemplate", true);
            mv.addObject("prd_template", templateService.processTemplate(product, new HashMap<>(mv.getModel())));
        } else {
            mv.addObject("prd_hasTemplate", false);
        }

    }

    private void loadStockDetails(Product product, ModelAndView mv) {
        QueryParameters sdparams = QueryParameters.with("product", product).orderBy("store.contactInfo.city asc, store.name", true);
        List<ProductStock> stockDetails = crudService.find(ProductStock.class, sdparams);
        Collection<CollectionWrapper> stockDetailsGroups = CollectionsUtils.groupBy(stockDetails, ProductStock.class, "store.contactInfo.city");
        CollectionWrapper firtGroup = CollectionsUtils.findFirst(stockDetailsGroups);
        if(firtGroup!=null){
            firtGroup.setDescription("active");
        }
        mv.addObject("prd_stock_details",stockDetailsGroups);
    }

}
