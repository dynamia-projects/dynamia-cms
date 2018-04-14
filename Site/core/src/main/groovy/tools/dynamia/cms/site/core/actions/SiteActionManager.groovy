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
package tools.dynamia.cms.site.core.actions

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
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
                return object.getName().equals(actionName)
            }
        })

        if (actions.isEmpty()) {
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
        ActionEvent evt = newEvent(SiteContext.get().getCurrent(), mv, CMSUtil.getCurrentRequest(), null, data)
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
        SiteService service = Containers.get().findObject(SiteService.class)
        Site site = service.getSite(request)

        if (site == null) {
            site = service.getMainSite()
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
