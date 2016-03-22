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
package com.dynamia.cms.site.products.services.impl;

import com.dynamia.cms.site.core.StringParser;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.domain.ProductTemplate;
import com.dynamia.cms.site.products.services.ProductTemplateService;
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

    private void loadDefaultTemplateModel(Product product, Map<String, Object> templateModel) {
        templateModel.putAll(BeanUtils.getValuesMaps("", product));

        templateModel.putAll(BeanUtils.getValuesMaps("brand_", product.getBrand()));
        templateModel.putAll(BeanUtils.getValuesMaps("category_", product.getCategory()));

        for (ProductDetail detail : product.getDetails()) {
            String name = detail.getName().toLowerCase().trim().replace(" ", "_");
            String value = detail.getValue() + " " + detail.getDescription();
            value = value.replace("null", "").trim();
            templateModel.put(name, value);
            templateModel.put(name + "_imageURL", detail.getImageURL());
        }

    }

}
