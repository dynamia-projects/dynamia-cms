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
package tools.dynamia.cms.products.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.StringParser
import tools.dynamia.cms.core.StringParsers
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductDetail
import tools.dynamia.cms.products.domain.ProductTemplate
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class ProductTemplateServiceImpl implements tools.dynamia.cms.products.services.ProductTemplateService {

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
