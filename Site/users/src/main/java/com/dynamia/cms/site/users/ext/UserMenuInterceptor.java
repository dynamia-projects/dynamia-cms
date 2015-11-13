/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.actions.UserMenuAction;

/**
 *
 * @author mario_2
 */
@CMSExtension
public class UserMenuInterceptor extends SiteRequestInterceptorAdapter {

    @Autowired
    private List<UserMenuAction> actions;

    @Override
    protected void afterRequest(Site site, ModelAndView modelAndView) {
        List<UserMenuAction> orderedActions = new ArrayList<>(actions);
        Collections.sort(orderedActions, new Comparator<UserMenuAction>() {

            @Override
            public int compare(UserMenuAction t, UserMenuAction t1) {
                Integer ua = t.getOrder();
                Integer ub = t1.getOrder();
                return ua.compareTo(ub);
            }
        });

        modelAndView.addObject("userMenuActions", orderedActions);

        if (UserHolder.get().isAuthenticated() && site == null) {
            modelAndView.addObject("site", UserHolder.get().getCurrent().getSite());
        }
    }

}
