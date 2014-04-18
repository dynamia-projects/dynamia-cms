/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.actions;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.ObjectMatcher;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public class SiteActionManager {

    public static SiteAction getAction(final String actionName) {
        Collection<SiteAction> actions = Containers.get().findObjects(SiteAction.class, new ObjectMatcher<SiteAction>() {

            @Override
            public boolean match(SiteAction object) {
                return object.getName().equals(actionName);
            }
        });

        if (actions.isEmpty()) {
            throw new SiteActionNotFoundException(actionName, "SiteAction with name '" + actionName + "' not found");
        } else if (actions.size() > 1) {
            throw new MoreThanOneActionException(actionName, actions.size() + " actions with the same name '" + actionName + "' found");
        }

        return (SiteAction) actions.toArray()[0];
    }

    public static void performAction(String actionName, ActionEvent evt) {
        SiteAction action = getAction(actionName);
        action.actionPerformed(evt);
    }

    public static void performAction(String actionName, Site site, ModelAndView mv, HttpServletRequest request, Object source, Object data) {
        ActionEvent evt = newEvent(site, mv, request, source, data);
        performAction(actionName, evt);
    }

    public static void performAction(String actionName, Site site, ModelAndView mv, HttpServletRequest request) {
        ActionEvent evt = newEvent(site, mv, request, null, null);
        performAction(actionName, evt);
    }

    public static void performAction(String actionName, Site site, ModelAndView mv, HttpServletRequest request, Object data) {
        ActionEvent evt = newEvent(site, mv, request, null, data);
        performAction(actionName, evt);
    }

    public static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, Object data) {
        performAction(actionName, mv, request, null, data);
    }

    public static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, HttpServletResponse response, Object data) {
        SiteService service = Containers.get().findObject(SiteService.class);
        Site site = service.getSite(request);

        if (site == null) {
            site = service.getMainSite();
        }

        ActionEvent evt = newEvent(site, mv, request, response, null, data);
        performAction(actionName, evt);
    }

    public static void performAction(String actionName, ModelAndView mv, HttpServletRequest request) {
        performAction(actionName, mv, request, null);
    }

    public static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {
        performAction(actionName, mv, request, response, null);
    }

    public static ActionEvent newEvent(Site site, ModelAndView mv, HttpServletRequest request, Object source, Object data) {
        return new ActionEvent(site, mv, request, null, source, data);
    }

    public static ActionEvent newEvent(Site site, ModelAndView mv, HttpServletRequest request, HttpServletResponse response, Object source, Object data) {
        return new ActionEvent(site, mv, request, response, source, data);
    }

}
