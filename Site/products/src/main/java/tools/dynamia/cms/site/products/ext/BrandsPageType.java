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
package tools.dynamia.cms.site.products.ext;

import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.actions.SiteActionManager;
import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.pages.PageContext;
import tools.dynamia.cms.site.pages.api.PageTypeExtension;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
public class BrandsPageType implements PageTypeExtension {

    @Override
    public String getId() {
        return "products-brands";
    }

    @Override
    public String getName() {
        return "Product Brands List Page";
    }

    @Override
    public String getDescription() {
        return "A brand list page, you cant specified a brand id using page parameters";
    }

    @Override
    public String getDescriptorId() {
        return "BrandGridConfig";
    }

    @Override
    public void setupPage(PageContext context) {
        ModelAndView mv = context.getModelAndView();
        mv.addObject("subtitle", context.getPage().getSubtitle());

        SiteActionManager.performAction("showBrands", mv, context.getRequest());

    }

}
