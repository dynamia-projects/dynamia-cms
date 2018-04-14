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

import tools.dynamia.cms.site.core.CMSException

/**
 *
 * @author Mario Serrano Leones
 */
class SiteActionNotFoundException extends CMSException {

    private String actionName

    SiteActionNotFoundException(String actionName) {
        this.actionName = actionName

    }

    SiteActionNotFoundException(String actionName, String message) {
        super(message)
        this.actionName = actionName
    }

    SiteActionNotFoundException(String actionName, String message, Throwable cause) {
        super(message, cause)
        this.actionName = actionName
    }

    SiteActionNotFoundException(String actionName, Throwable cause) {
        super(cause)
        this.actionName = actionName
    }

    SiteActionNotFoundException(String actionName, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
        this.actionName = actionName
    }

    String getActionName() {
        return actionName
    }

}
