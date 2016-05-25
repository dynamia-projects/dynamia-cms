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
package tools.dynamia.cms.site.products.services.impl;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.StringParser;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.ProductDetail;
import tools.dynamia.cms.site.products.domain.ProductTemplate;
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig;
import tools.dynamia.cms.site.products.services.ProductTemplateService;
import tools.dynamia.cms.site.products.services.ProductsService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.dynamia.commons.BeanUtils;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class ProductTemplateServiceImpl implements ProductTemplateService {

    private static final String CACHE_NAME = "products";

    @Autowired
    @Qualifier("mustacheStringParser")
    private StringParser stringParser;

    @Autowired
    private CrudService crudService;

    @Autowired
    private ProductsService productsService;

    /**
     * Process product template using VelocityTemplate Engine
     *
     * @param product
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "'template'+#product.id")
    public String processTemplate(Product product, Map<String, Object> templateModel) {

        product = crudService.reload(product);

        ProductTemplate template = getTemplate(product);
        if (template == null) {
            return null;
        }

        if (templateModel == null) {
            templateModel = new HashMap<>();
        }

        loadDefaultTemplateModel(product, templateModel);

        return stringParser.parse(template.getContent(), templateModel);

    }

    /**
     * Check if product has an enabled template. Internally it just check if
     * getTemplate(product) result is not null
     *
     * @param product
     * @return
     */
    @Override
    public boolean hasTemplate(Product product) {
        ProductTemplate template = getTemplate(product);
        return template != null;
    }

    /**
     * Get an enabled product template from product itself or product category
     * or product category parent. If product template is not enabled return
     * null
     *
     * @param product
     * @return
     */
    @Override
    public ProductTemplate getTemplate(Product product) {
        ProductTemplate template = product.getTemplate();
        if (template == null && product.getCategory() != null) {
            template = product.getCategory().getTemplate();
        }

        if (template == null && product.getCategory().getParent() != null) {
            template = product.getCategory().getParent().getTemplate();
        }

        if (template != null && !template.isEnabled()) {
            template = null;
        }

        return template;
    }

    @Override
    public void loadDefaultTemplateModel(Product product, Map<String, Object> templateModel) {
        Site site = product.getSite();
        CMSUtil util = new CMSUtil(site);
        templateModel.putAll(BeanUtils.getValuesMaps("", product));
        ProductsSiteConfig config = productsService.getSiteConfig(site);

        templateModel.put("imageURL", getImageURL(site, product.getImage()));
        templateModel.put("image2URL", getImageURL(site, product.getImage2()));
        templateModel.put("image3URL", getImageURL(site, product.getImage3()));
        templateModel.put("image4URL", getImageURL(site, product.getImage4()));
        templateModel.put("priceFormatted", util.formatNumber(product.getPrice(), config.getPricePattern()));
        templateModel.put("lastPriceFormatted", util.formatNumber(product.getLastPrice(), config.getPricePattern()));
        templateModel.put("storePriceFormatted", util.formatNumber(product.getStorePrice(), config.getPricePattern()));
        templateModel.put("realPriceFormatted", util.formatNumber(product.getRealPrice(), config.getPricePattern()));
        templateModel.put("realLastPriceFormatted", util.formatNumber(product.getRealLastPrice(), config.getPricePattern()));

        templateModel.putAll(BeanUtils.getValuesMaps("brand_", product.getBrand()));
        templateModel.putAll(BeanUtils.getValuesMaps("category_", product.getCategory()));

        templateModel.put("brand", product.getBrand().getName());
        templateModel.put("brand_imageURL", CMSUtil.getSiteURL(site, "resources/products/brands/thumbnails/" + product.getBrand().getImage()));
        templateModel.put("category", product.getCategory().getName());

        for (ProductDetail detail : product.getDetails()) {
            String name = detail.getName().toLowerCase().trim().replace(" ", "_");
            String value = detail.getValue() + " " + detail.getDescription();
            value = value.replace("null", "").trim();
            templateModel.put(name, value);
            templateModel.put(name + "_imageURL", detail.getImageURL());
        }

        //Actions
        String productURL = CMSUtil.getSiteURL(site, "store/products/" + product.getId());
        templateModel.put("productURL", productURL);
        String actionPath = CMSUtil.getSiteURL(site, "shoppingcart/");
        templateModel.put("action_addCart", actionPath + "shop/add/" + product.getId() + "?currentURI=/store/products/" + product.getId());
        templateModel.put("action_addQuote", actionPath + "quote/add/" + product.getId() + "?currentURI=/store/products/" + product.getId());
        templateModel.put("action_compare", productURL+"/compare");
        templateModel.put("action_favorite", productURL+"/favorite");
        templateModel.put("action_print", productURL+"/print");
        templateModel.put("action_share", productURL+"#shareProduct"+product.getId());

    }

    private Object getImageURL(Site site, String image) {
        if (image != null && !image.isEmpty()) {
            return CMSUtil.getSiteURL(site, "resources/products/images/" + image);
        } else {
            return "";
        }
    }

}
