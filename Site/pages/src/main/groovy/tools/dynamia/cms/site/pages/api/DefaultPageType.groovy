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
package tools.dynamia.cms.site.pages.api

import tools.dynamia.cms.site.core.api.CMSExtension
import tools.dynamia.cms.site.pages.PageContext

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
public class DefaultPageType implements PageTypeExtension {

    @Override
    public String getId() {
        return "default";
    }

    @Override
    public String getName() {
        return "Default Page";
    }

    @Override
    public String getDescription() {
        return "Render the content in plain html";
    }

    @Override
    public String getDescriptorId() {
        return null;
    }

    @Override
    public void setupPage(PageContext context) {
        context.getModelAndView().setViewName("site/page");
    }

}
