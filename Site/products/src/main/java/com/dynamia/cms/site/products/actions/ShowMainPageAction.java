/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.controllers.StoreController;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.services.CrudService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
public class ShowMainPageAction implements SiteAction {

    private LoggingService logger = new SLF4JLoggingService(ShowMainPageAction.class);

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return "showMainProductPage";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        ModelAndView mv = evt.getModelAndView();

        List<Product> specialProducts = service.getSpecialProducts(evt.getSite());

        List<Product> mostViewed = service.getMostViewedProducts(evt.getSite());

        mv.addObject("prd_mostViewedProducts", mostViewed);
        mv.addObject("prd_specialProducts", specialProducts);
        if (UserHolder.get().isAuthenticated()) {
            loadRecentProductsFromUser(evt, mv);
        } else {
            loadRecentProductsFromCookies(evt, mv);
        }

    }

    private void loadRecentProductsFromUser(ActionEvent evt, ModelAndView mv) {
        try {
            List<Product> recentViewed = service.getRecentProducts(UserHolder.get().getCurrent());
            Product firstProduct = recentViewed.get(0);
            List<Product> relatedProducts = service.getRelatedProducts(firstProduct);
            mv.addObject("prd_recentViewedProducts", recentViewed);
            mv.addObject("prd_relatedProducts", relatedProducts);
        } catch (Exception e) {
            System.out.println("ERROR loadRecentProductsFromUser " + e.getMessage());
        }

    }

    private void loadRecentProductsFromCookies(ActionEvent evt, ModelAndView mv) throws NumberFormatException {
        try {
            Cookie cookie = CMSUtil.getCookie(evt.getRequest(), StoreController.RECENT_PRODUCTS_COOKIE_NAME + evt.getSite().getKey());
            if (cookie != null) {
                List<Long> ids = new ArrayList<>();
                String values[] = StringUtils.arraySplit(cookie.getValue(), ',', true);
                for (String idText : values) {
                    ids.add(new Long(idText));
                }
                if (!ids.isEmpty()) {

                    List<Product> recentViewed = service.getProductsById(ids);
                    recentViewed = sortByIdList(recentViewed, ids);

                    Product firstProduct = recentViewed.get(0);
                    List<Product> relatedProducts = service.getRelatedProducts(firstProduct);
                    mv.addObject("prd_recentViewedProducts", recentViewed);
                    mv.addObject("prd_relatedProducts", relatedProducts);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error processing RECENT PRODUCTS COOKIE", e);
        }
    }

    private List<Product> sortByIdList(List<Product> list, List<Long> ids) {
        List<Product> resultList = new ArrayList<>();
        for (Long id : ids) {
            for (Product product : list) {
                if (product.getId().equals(id)) {
                    resultList.add(product);
                    break;
                }
            }
        }
        return resultList;

    }

}
