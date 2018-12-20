/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
