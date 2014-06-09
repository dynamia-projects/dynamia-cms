/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.controller;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author mario_2
 */
@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @RequestMapping(value = "/add/{itemCode}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView add(@PathVariable String itemCode, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        String redirect = request.getParameter("currentURI");

        mv.setView(new RedirectView(redirect, true, true, false));
        SiteActionManager.performAction("addItemToCart", mv, request, redirectAttributes, itemCode);

        return mv;
    }

    @RequestMapping(value = "/remove/{itemCode}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView remove(@PathVariable String itemCode, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        String redirect = request.getParameter("currentURI");
        mv.setView(new RedirectView(redirect, true, true, false));
        SiteActionManager.performAction("removeItemFromCart", mv, request, redirectAttributes, itemCode);

        return mv;
    }

    @RequestMapping(value = "/clear", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView clear(HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        String redirect = request.getParameter("currentURI");
        mv.setView(new RedirectView(redirect, true, true, false));
        SiteActionManager.performAction("clearShoppingCart", mv, request, redirectAttributes, null);

        return mv;
    }

}
