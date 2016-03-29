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
package com.dynamia.cms.admin.core;

import tools.dynamia.integration.sterotypes.Component;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.FieldCustomizer;

@Component
public class SiteAwareFieldCustomizer implements FieldCustomizer {

    @Override
    public void customize(String view, Field field) {
        if (field.getName().equals("site")) {
            field.setVisible(false);
        }
    }

}
