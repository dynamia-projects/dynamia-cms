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
package tools.dynamia.cms.core.actions

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.integration.Containers
import tools.dynamia.integration.ObjectMatcher

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
class SiteActionManager {

    static SiteAction getAction(final String actionName) {
        Collection<SiteAction> actions = Containers.get().findObjects(SiteAction.class, new ObjectMatcher<SiteAction>() {

            @Override
            boolean match(SiteAction object) {
                return object.name.equals(actionName)
            }
        })

        if (actions.empty) {
            throw new SiteActionNotFoundException(actionName, "SiteAction with name '" + actionName + "' not found")
        } else if (actions.size() > 1) {
            throw new MoreThanOneActionException(actionName, actions.size() + " actions with the same name '" + actionName + "' found")
        }

        return (SiteAction) actions.toArray()[0]
    }

    static void performAction(String actionName, ActionEvent evt) {
        SiteAction action = getAction(actionName)
        action.actionPerformed(evt)
    }

    static void performAction(String actionName, ModelAndView mv, Object data) {
        ActionEvent evt = newEvent(SiteContext.get().current, mv, CMSUtil.currentRequest, null, data)
        performAction(actionName, evt)
    }

    static void performAction(String actionName, Site site, ModelAndView mv, HttpServletRequest request, Object source, Object data) {
        ActionEvent evt = newEvent(site, mv, request, source, data)
        performAction(actionName, evt)
    }

    static void performAction(String actionName, Site site, ModelAndView mv, HttpServletRequest request) {
        ActionEvent evt = newEvent(site, mv, request, null, null)
        performAction(actionName, evt)
    }

    static void performAction(String actionName, Site site, ModelAndView mv, HttpServletRequest request, Object data) {
        ActionEvent evt = newEvent(site, mv, request, null, data)
        performAction(actionName, evt)
    }

    static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, Object data) {
        performAction(actionName, mv, request, (HttpServletResponse) null, null, data)
    }

    static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, RedirectAttributes redirectAttributes,
                              Object data) {
        performAction(actionName, mv, request, (HttpServletResponse) null, redirectAttributes, data)
    }

    static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                              Object data) {
        performAction(actionName, mv, request, response, null, data)
    }

    static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                              RedirectAttributes redirectAttributes, Object data) {
        tools.dynamia.cms.core.services.SiteService service = Containers.get().findObject(tools.dynamia.cms.core.services.SiteService.class)
        Site site = service.getSite(request)

        if (site == null) {
            site = service.mainSite
        }

        ActionEvent evt = newEvent(site, mv, request, response, redirectAttributes, null, data)
        performAction(actionName, evt)
    }

    static void performAction(String actionName, ModelAndView mv, HttpServletRequest request) {
        performAction(actionName, mv, request, null)
    }

    static void performAction(String actionName, ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {
        performAction(actionName, mv, request, response, null, null)
    }

    static ActionEvent newEvent(Site site, ModelAndView mv, HttpServletRequest request, Object source, Object data) {
        return new ActionEvent(site, mv, request, null, null, source, data)
    }

    static ActionEvent newEvent(Site site, ModelAndView mv, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Object source, Object data) {
        return new ActionEvent(site, mv, request, null, redirectAttributes, source, data)
    }

    static ActionEvent newEvent(Site site, ModelAndView mv, HttpServletRequest request, HttpServletResponse response, Object source,
                                Object data) {
        return new ActionEvent(site, mv, request, response, null, source, data)
    }

    static ActionEvent newEvent(Site site, ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                                RedirectAttributes redirectAttributes, Object source, Object data) {
        return new ActionEvent(site, mv, request, response, redirectAttributes, source, data)
    }

}
