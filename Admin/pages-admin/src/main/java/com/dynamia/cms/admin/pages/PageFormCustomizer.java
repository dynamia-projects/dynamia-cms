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
package com.dynamia.cms.admin.pages;

import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.users.UserHolder;

import tools.dynamia.viewers.ViewCustomizer;
import tools.dynamia.zk.viewers.form.FormView;

public class PageFormCustomizer implements ViewCustomizer<FormView<Page>> {

    @Override
    public void customize(final FormView<Page> view) {

        if (UserHolder.get().isAuthenticated()) {
            view.addEventListener(FormView.ON_VALUE_CHANGED, evt -> {
                if (view.getValue().getAuthor() == null) {
                    view.getValue().setAuthor(UserHolder.get().getCurrent().getRelatedAuthor());
                }
            });
        }

    }

}