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
package com.dynamia.cms.admin.core.zk.ui;

import org.zkforge.ckez.CKeditor;
import tools.dynamia.commons.MapBuilder;

import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

public class CMSeditor extends CKeditor {

    /**
     *
     */
    private static final long serialVersionUID = -4372806043096340598L;

    static {
        ComponentAliasIndex.getInstance().add(CMSeditor.class);
        BindingComponentIndex.getInstance().put("value", CMSeditor.class);
        setFileBrowserTemplate("/browse");
        setFileUploadHandlePage("/browse");

    }

    public CMSeditor() {
        setFilebrowserBrowseUrl("resources");
        setFilebrowserUploadUrl("resources");
        setConfig(MapBuilder.put("config.entities_latin", false,
                "config.entities",false,
                "config.htmlEncodeOutput",false));
        
    }

}
