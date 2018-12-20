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
package tools.dynamia.cms.products.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import org.springframework.web.util.CookieGenerator
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.ProductShareForm
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.commons.StringUtils

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
@SessionAttributes("prd_searchForm")
@RequestMapping(value = "/store", method = RequestMethod.GET)
class StoreController {

    public final static String RECENT_PRODUCTS_COOKIE_NAME = "CMSRPRD"

    @Autowired
    private ProductsService service

    @Autowired
    private SiteService siteService

    @RequestMapping(value = ["", "/", "/categories"], method = RequestMethod.GET)
    ModelAndView category(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/categories")

        SiteActionManager.performAction("showProductCategories", mv, request)

        return mv

    }

    @RequestMapping(value = "/categories/{id}")
    ModelAndView categoryid(@PathVariable Long id, HttpServletRequest request) {
        return category(id, null, request)
    }

    @RequestMapping(value = "/categories/{id}/{category}")
    ModelAndView category(@PathVariable Long id, @PathVariable String category, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/products")

        SiteActionManager.performAction("loadProductCategory", mv, request, id)

        return mv

    }

    @RequestMapping("/categories/{id}/{category}/{brand}")
    ModelAndView categoryBrand(@PathVariable Long id, @PathVariable String category, @PathVariable String brand,
                               HttpServletRequest request) {
        Site site = siteService.getSite(request)

        ProductSearchForm form = new ProductSearchForm()
        ProductBrand productBrand = service.getBrandByAlias(site, brand)
        if (productBrand != null) {
            form.brandId = productBrand.id
        }
        form.categoryId = id

        ModelAndView mv = new ModelAndView("products/brand")
        SiteActionManager.performAction("showProductBrand", mv, request, form)
        return mv
    }

    @RequestMapping("/search")
    ModelAndView search(ProductSearchForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasFieldErrors("maxPrice")) {
            form.maxPrice = null
        }
        if (bindingResult.hasFieldErrors("minPrice")) {
            form.minPrice = null
        }

        ModelAndView mv = new ModelAndView("products/productquery")

        SiteActionManager.performAction("searchProducts", mv, request, form)

        return mv
    }

    @RequestMapping("/products/{id}")
    ModelAndView product(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("products/product")
        SiteActionManager.performAction("showProduct", mv, request, id)
        setupCookie(request, response, id)

        return mv
    }

    @RequestMapping("/products")
    ModelAndView product(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("products/product")
        SiteActionManager.performAction("showProduct", mv, request)

        return mv
    }

    @RequestMapping("/products/{id}/print")
    ModelAndView printProduct(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("products/printproduct")
        SiteActionManager.performAction("showProduct", mv, request, id)
        setupCookie(request, response, id)

        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping("/products/{id}/favorite")
    ModelAndView productFavorite(@PathVariable Long id, HttpServletRequest request,
                                 HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("products/product")
        if (UserHolder.get().authenticated) {
            System.out.println("USER LOGGED: " + UserHolder.get().userName)
        }
        mv = product(id, request, response)

        return mv
    }

    @RequestMapping("/products/{id}/compare")
    ModelAndView productCompare(@PathVariable Long id, HttpServletRequest request,
                                HttpServletResponse response) {
        ModelAndView mv = new ModelAndView()
        SiteActionManager.performAction("addProductToCompareList", mv, request, response, id)
        return mv
    }

    @RequestMapping("/products/{id}/compare/remove")
    ModelAndView productNoCompare(@PathVariable Long id, HttpServletRequest request,
                                  HttpServletResponse response) {
        ModelAndView mv = new ModelAndView()
        SiteActionManager.performAction("removeProductFromCompareList", mv, request, response, id)
        return mv
    }

    @RequestMapping("/brands")
    ModelAndView brands(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/brands")
        SiteActionManager.performAction("showProductBrands", mv, request)
        return mv
    }

    @RequestMapping("/brands/{name}")
    ModelAndView brand(@PathVariable String name, HttpServletRequest request) {
        Site site = siteService.getSite(request)

        ProductSearchForm form = new ProductSearchForm()
        try {
            if (name != null && !name.equals("#null#")) {
                form.brandId = service.getBrandByAlias(site, name).id
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        ModelAndView mv = new ModelAndView("products/brand")
        SiteActionManager.performAction("showProductBrand", mv, request, form)
        return mv
    }

    @RequestMapping("/compare/{ids}")
    ModelAndView compare(@PathVariable String ids, HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("products/compare")
        SiteActionManager.performAction("compareProducts", mv, request, redirectAttributes, ids.split(","))
        return mv
    }

    @RequestMapping("/compare/clear")
    ModelAndView compareClear(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("products/compare")
        SiteActionManager.performAction("compareClear", mv, request, redirectAttributes)
        return mv
    }

    @RequestMapping("/compare/{ids}/print")
    ModelAndView comparePrint(@PathVariable String ids, HttpServletRequest request) {
        Site site = siteService.getSite(request)

        ModelAndView mv = new ModelAndView("products/printcompare")
        SiteActionManager.performAction("compareProducts", mv, request, ids.split(","))
        return mv
    }

    @RequestMapping("/stores")
    ModelAndView stores(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/stores")
        SiteActionManager.performAction("showProductStores", mv, request)
        return mv
    }

    @RequestMapping(value = "/products/{id}/share", method = RequestMethod.GET)
    ModelAndView share(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("products/share")
        SiteActionManager.performAction("showProduct", mv, id)
        return mv
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    ModelAndView share(@Valid ProductShareForm form, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView()
        mv.view = new RedirectView("/store/products/" + form.productId, true, true, false)
        if (!bindingResult.hasErrors()) {
            form.site = SiteContext.get().current
            form.productURL = SiteContext.get().siteURL + "/store/products/" + form.productId
            SiteActionManager.performAction("shareProduct", mv, request, redirectAttributes, form)
        } else {
            CMSUtil.addErrorMessage("Todos los campos son requeridos para enviar a un amigo", redirectAttributes)
        }

        return mv
    }

    private void setupCookie(HttpServletRequest request, HttpServletResponse response, Long id) {

        Site site = siteService.getSite(request)

        if (site == null) {
            return
        }

        String idText = String.valueOf(id)
        String siteCookieName = RECENT_PRODUCTS_COOKIE_NAME + site.key

        Cookie currentCookie = CMSUtil.getCookie(request, siteCookieName)

        List<String> ids = new ArrayList<>()
        if (currentCookie != null) {
            String[] value = currentCookie.value.split(",")
            ids.addAll(Arrays.asList(value))
        }

        if (ids.contains(idText)) {
            ids.remove(idText)
        }

        ids.add(0, idText)

        if (ids.size() > service.getSiteConfig(site).productsPerPage) {
            ids.remove(ids.size() - 1)
        }

        CookieGenerator recentViews = new CookieGenerator()
        recentViews.cookieName = siteCookieName
        recentViews.cookieMaxAge = 30 * 24 * 60 * 60
        recentViews.addCookie(response, StringUtils.arrayToCommaDelimitedString(ids.toArray()))
    }

}
