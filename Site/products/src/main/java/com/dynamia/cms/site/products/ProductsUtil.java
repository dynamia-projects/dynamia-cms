/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.commons.collect.PagedList;
import com.dynamia.tools.commons.collect.PagedListDataSource;
import com.dynamia.tools.integration.Containers;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public class ProductsUtil {

    private static final String DATASOURCE = "productsDatasource";

    public static void setupDefaultVars(Site site, ModelAndView mv) {
        ProductsService service = Containers.get().findObject(ProductsService.class);

        mv.addObject("prd_categories", service.getCategories(site));
        mv.addObject("prd_brands", service.getBrands(site));
        mv.addObject("prd_config", service.getSiteConfig(site));
        if (mv.getModel().get("prd_searchForm") == null) {
            mv.addObject("prd_searchForm", new ProductSearchForm());
        }
    }

    public static void setupProductsVar(List<Product> products, ModelAndView mv) {
        mv.addObject("prd_products", products);
    }

    public static void setupProductsVar(List<Product> products, Map<String, Object> map) {
        map.put("prd_products", products);
    }

    public static void setupProductVar(Product product, ModelAndView mv) {
        mv.addObject("product", product);
    }

    public static List<Product> setupPagination(List<Product> products, HttpServletRequest request, ModelAndView mv) {
        PagedListDataSource datasource = (PagedListDataSource) request.getSession().getAttribute(DATASOURCE);

        if (products instanceof PagedList) {
            datasource = ((PagedList) products).getDataSource();
            request.getSession().setAttribute(DATASOURCE, datasource);
        }

        if (datasource != null) {
            mv.addObject(DATASOURCE, datasource);

            if (request.getParameter("page") != null) {
                try {
                    int page = Integer.parseInt(request.getParameter("page"));
                    datasource.setActivePage(page);
                } catch (NumberFormatException numberFormatException) {
                    //not a number, ignore it
                }
            }

            products = datasource.getPageData();
        }
        return products;
    }

}
