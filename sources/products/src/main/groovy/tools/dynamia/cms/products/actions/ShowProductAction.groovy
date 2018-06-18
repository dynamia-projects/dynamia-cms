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
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.pages.PageNotFoundException
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductStock
import tools.dynamia.cms.products.domain.ProductUserStory
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.domain.RelatedProduct
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.commons.collect.CollectionWrapper
import tools.dynamia.commons.collect.CollectionsUtils
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

/**
 * @author Mario Serrano Leones
 */
@CMSAction
class ShowProductAction implements SiteAction {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
    private tools.dynamia.cms.products.services.ProductTemplateService templateService

    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "showProduct"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView

        boolean simpleMode = false
        if ("simple".equals(evt.request.getParameter("mode"))) {
            mv.viewName = "products/productsimple"
            simpleMode = true
        }

        loadProduct(evt, mv, simpleMode)

    }

    private void loadProduct(ActionEvent evt, ModelAndView mv, boolean simpleMode) {
        Product product = null
        Long id = (Long) evt.data
        QueryParameters qp = QueryParameters.with("active", true).add("site", evt.site)

        if (id != null) {
            qp.add("id", id)
        } else if (evt.request.getParameter("sku") != null) {
            String sku = evt.request.getParameter("sku")
            qp.add("sku", sku)

        }
        product = crudService.findSingle(Product.class, qp)

        if (product == null) {
            throw new PageNotFoundException("Product not found")
        }

        String price = ""
        try {
            CMSUtil util = new CMSUtil(evt.site)
            ProductsSiteConfig config = service.getSiteConfig(evt.site)
            price = " - " + util.formatNumber(product.price, config.pricePattern)
        } catch (Exception e) {
        }

        service.updateViewsCount(product)
        service.updateProductStoryViews(product)
        ProductUserStory story = service.getProductStory(product, UserHolder.get().current)
        if (story != null) {
            mv.addObject("prd_story", story)
        }

        loadStockDetails(product, mv)
        loadRelatedProducts(product, mv)
        loadGiftsProducts(product, mv)
        loadReviews(product, mv)

        mv.addObject("prd_product", product)
        mv.addObject("prd_config", service.getSiteConfig(evt.site))
        mv.addObject("title", product.name.toUpperCase() + price)
        mv.addObject("subtitle", product.category.name)
        mv.addObject("icon", "info-sign")

        mv.addObject("metaDescription", product.description)
        if (product.tags != null && !product.tags.empty) {
            mv.addObject("metaKeywords", product.tags)
        }

        String baseImageUrl = SiteContext.get().siteURL + "/resources/products/images/"
        List<String> pageImages = new ArrayList<>()
        pageImages.add(baseImageUrl + product.image)
        if (product.image2 != null) {
            pageImages.add(baseImageUrl + product.image2)
        }
        if (product.image3 != null) {
            pageImages.add(baseImageUrl + product.image3)
        }
        if (product.image4 != null) {
            pageImages.add(baseImageUrl + product.image4)
        }
        mv.addObject("pageImages", pageImages)
        mv.addObject("baseImageUrl", baseImageUrl)

        if (templateService.hasTemplate(product)) {
            mv.addObject("prd_hasTemplate", true)
            if (simpleMode) {
                mv.addObject("prd_template",
                        templateService.processAlternateTemplate(product, new HashMap<>(mv.model)))
            } else {
                mv.addObject("prd_template", templateService.processTemplate(product, new HashMap<>(mv.model)))
            }
        } else {
            mv.addObject("prd_hasTemplate", false)
        }
    }

    private void loadReviews(Product product, ModelAndView mv) {

        mv.addObject("reviews", service.getTopReviews(product, 30))
    }

    private void loadRelatedProducts(Product product, ModelAndView mv) {
        List<Product> products = null
        List<RelatedProduct> relateds = service.getRelatedProducts(product, false)
        if (relateds != null && !relateds.empty) {
            products = relateds.collect { it.product }
        } else {
            products = service.getRelatedCategoryProducts(product)
        }
        mv.addObject("prd_relatedProducts", products)
    }

    private void loadStockDetails(Product product, ModelAndView mv) {
        QueryParameters sdparams = QueryParameters.with("product", product)
                .orderBy("store.contactInfo.city asc, store.name", true)
        List<ProductStock> stockDetails = crudService.find(ProductStock.class, sdparams)

        Collection<CollectionWrapper> stockDetailsGroups = CollectionsUtils.groupBy(stockDetails, ProductStock.class,
                "store.contactInfo.city")
        CollectionWrapper firtGroup = CollectionsUtils.findFirst(stockDetailsGroups)
        if (firtGroup != null) {
            firtGroup.description = "active"
        }
        mv.addObject("prd_stock_details", stockDetailsGroups)
    }

    private void loadGiftsProducts(Product product, ModelAndView mv) {
        List<RelatedProduct> gifts = service.getRelatedProducts(product, true)
        List<Product> products = gifts.findResults { it.gift ? it.product : null }
        mv.addObject("prd_gifts", products)

    }

}