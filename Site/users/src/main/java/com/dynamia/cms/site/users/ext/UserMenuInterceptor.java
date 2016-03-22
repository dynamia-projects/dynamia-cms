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
 * @author Mario Serrano Leones
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
