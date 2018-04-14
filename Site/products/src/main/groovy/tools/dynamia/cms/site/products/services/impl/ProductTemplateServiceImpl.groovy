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
package tools.dynamia.cms.site.products.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.StringParser
import tools.dynamia.cms.site.core.StringParsers
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.domain.ProductDetail
import tools.dynamia.cms.site.products.domain.ProductTemplate
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig
import tools.dynamia.cms.site.products.services.ProductTemplateService
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class ProductTemplateServiceImpl implements ProductTemplateService {

	private static final String CACHE_NAME = "velocity"

	@Autowired
	private CrudService crudService

	@Autowired
	private ProductsService productsService

	/**
	 * Process product template using VelocityTemplate Engine
	 *
	 * @param product
	 * @return
	 */
	@Override
	@Cacheable(value = ProductTemplateServiceImpl.CACHE_NAME, key = "'template'+#product.id")
	String processTemplate(Product product, Map<String, Object> templateModel) {

		product = crudService.reload(product)

		ProductTemplate template = getTemplate(product)
		return processTemplate(product, templateModel, template)

	}

	@Override
	@Cacheable(value = ProductTemplateServiceImpl.CACHE_NAME, key = "'altTemplate'+#product.id")
	String processAlternateTemplate(Product product, Map<String, Object> templateModel) {

		product = crudService.reload(product)

		ProductTemplate template = getAlternateTemplate(product)
		if (template == null) {
			template = getTemplate(product)
		}
		return processTemplate(product, templateModel, template)

	}

	String processTemplate(Product product, Map<String, Object> templateModel, ProductTemplate template) {
		if (template == null) {
			return null
		}

		if (templateModel == null) {
			templateModel = new HashMap<>()
		}

		loadDefaultTemplateModel(product, templateModel)
		StringParser stringParser = StringParsers.get(template.templateEngine)
		return stringParser.parse(template.content, templateModel)
	}

	/**
	 * Check if product has an enabled template. Internally it just check if
	 * getTemplate(product) result is not null
	 *
	 * @param product
	 * @return
	 */
	@Override
	boolean hasTemplate(Product product) {
		ProductTemplate template = getTemplate(product)
		ProductTemplate alternateTemplate = getAlternateTemplate(product)
		return template != null || alternateTemplate != null
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
	ProductTemplate getTemplate(Product product) {
		ProductTemplate template = product.template
		if (template == null && product.category != null) {
			template = product.category.template
		}

		if (template == null && product.category.parent != null) {
			template = product.category.parent.template
		}

		if (template != null && !template.enabled) {
			template = null
		}

		return template
	}

	ProductTemplate getAlternateTemplate(Product product) {
		ProductTemplate template = product.alternateTemplate
		if (template == null && product.category != null) {
			template = product.category.alternateTemplate
		}

		if (template == null && product.category.parent != null) {
			template = product.category.parent.alternateTemplate
		}

		if (template != null && !template.enabled) {
			template = null
		}

		return template
	}

	@Override
	void loadDefaultTemplateModel(Product product, Map<String, Object> templateModel) {
		Site site = product.site
		CMSUtil util = new CMSUtil(site)
		templateModel.putAll(BeanUtils.getValuesMaps("", product))
		ProductsSiteConfig config = productsService.getSiteConfig(site)

		templateModel.put("imageURL", getImageURL(site, product.image))
		templateModel.put("image2URL", getImageURL(site, product.image2))
		templateModel.put("image3URL", getImageURL(site, product.image3))
		templateModel.put("image4URL", getImageURL(site, product.image4))
		templateModel.put("priceFormatted", util.formatNumber(product.price, config.pricePattern))
		templateModel.put("lastPriceFormatted", util.formatNumber(product.lastPrice, config.pricePattern))
		templateModel.put("storePriceFormatted", util.formatNumber(product.storePrice, config.pricePattern))
		templateModel.put("realPriceFormatted", util.formatNumber(product.realPrice, config.pricePattern))
		templateModel.put("realLastPriceFormatted",
				util.formatNumber(product.realLastPrice, config.pricePattern))

		templateModel.putAll(BeanUtils.getValuesMaps("brand_", product.brand))
		templateModel.putAll(BeanUtils.getValuesMaps("category_", product.category))

		templateModel.put("brand", product.brand.name)
		templateModel.put("brand_imageURL",
				CMSUtil.getSiteURL(site, "resources/products/brands/thumbnails/" + product.brand.image))
		templateModel.put("category", product.category.name)

		for (ProductDetail detail : (product.details)) {
			String name = detail.name.toLowerCase().trim().replace(" ", "_").replace(".", "").replace(":", "")
			String value = detail.value + " " + detail.description
			value = value.replace("null", "").trim()
			templateModel.put(name, value)
			templateModel.put(name + "_imageURL", detail.imageURL)
			templateModel.put(name + "_url", detail.url)
			templateModel.put(name + "_url2", detail.url2)
			templateModel.put(name + "_color", detail.color)
			templateModel.put(name + "_value2", detail.value2)
		}

		// Actions
		String productURL = CMSUtil.getSiteURL(site, "store/products/" + product.id)
		templateModel.put("productURL", productURL)
		String actionPath = CMSUtil.getSiteURL(site, "shoppingcart/")
		templateModel.put("action_addCart",
				actionPath + "shop/add/" + product.id + "?currentURI=/store/products/" + product.id)
		templateModel.put("action_addQuote",
				actionPath + "quote/add/" + product.id + "?currentURI=/store/products/" + product.id)
		templateModel.put("action_compare", productURL + "/compare")
		templateModel.put("action_favorite", productURL + "/favorite")
		templateModel.put("action_print", productURL + "/print")
		templateModel.put("action_share", productURL + "#shareProduct" + product.id)

	}

	private Object getImageURL(Site site, String image) {
		if (image != null && !image.empty) {
			return CMSUtil.getSiteURL(site, "resources/products/images/" + image)
		} else {
			return ""
		}
	}

}
